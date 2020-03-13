package com.yw.ojproject.service.impl;

import com.yw.ojproject.bo.ClassTypeBo;
import com.yw.ojproject.bo.ColumnConditionBO;
import com.yw.ojproject.service.BaseServer;
import com.yw.ojproject.utils.ClassHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-03-13 00:10
**/
@Slf4j
public class BaseServerImpl<T> implements BaseServer<T> {
    /**
     * 实现的dao
     * 需要在子类中具体注入
     */
    protected JpaSpecificationExecutor dao;

    /**
     * 实体管理类，使用统计求和查询时用到
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * 类型
     */
    private Class clazz;

    /**
     * 根据条件分页查询，条件拼接方式为 and
     * 条件拼接方式  xx字段_xx操作,比如等于查询 xxx_eq=xxx
     * 具体操作符查看 ColumnConditionBO
     * @param params
     * @return
     */
    @Override
    public Page<T> findAllPageByParams(Map<String,String> params){
        Specification<T> specification = this.getSpecification(params);
        return dao.findAll(specification, getPageable(params));
    }

    /**
     * 求和数据
     * 条件同上（分页条件查询） 注意~！！！ 不需要分页属性，但是要添加统计的条件
     * 求和统计条件  xxx字段_sum = xx类型（支持BigDecimal 和 Long）
     *
     * @param params 参数
     * @return 查询条件
     */
    @Override
    public Map<String, Object> getSumByParams(Map<String, String> params) throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        clazz = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Root<T> root = query.from(clazz);

        List<Predicate> predicateListNew = getPredicateList(params,cb,root);

        Predicate[] pred = new Predicate[predicateListNew.size()];
        pred = predicateListNew.toArray(pred);


        query.where(pred);
        Set<String> keySet = params.keySet();
        List<Selection<?>> selections = new ArrayList<Selection<?>>();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if (key.contains("_sum")) {
                if ("BigDecimal".equals(params.get(key))) {
                    selections.add(cb.sum(root.get(key.split("_")[0]).as(BigDecimal.class)));
                } else {
                    selections.add(cb.sum(root.get(key.split("_")[0]).as(Long.class)));
                }
            }
        }
        query.multiselect(selections);
        TypedQuery<Tuple> q = em.createQuery(query);
        List<Tuple> result = q.getResultList();
        Tuple tuple = result.get(0);
        Map<String, Object> param = new HashMap<>();
        int i = 0;
        Iterator<String> resultParam = keySet.iterator();
        while (resultParam.hasNext()) {
            String key = resultParam.next();
            if (key.contains("_sum")) {
                param.put(key, tuple.get(i));
                i++;
            }
        }
        return param;
    }

    /**
     * 获取查询所需要的参数
     * @param params
     * @return
     */
    private Specification<T> getSpecification(Map<String,String> params) {
        return  ((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = this.getPredicateList(params,criteriaBuilder,root);
            Predicate[] p = new Predicate[predicateList.size()];
            return criteriaBuilder.and(predicateList.toArray(p));
        });
    }


    /**
     * 获取分页属性
     * @param map
     * @return
     */
    private Pageable getPageable(Map<String, String> map) {
        int page;
        int size;
        try {
            page = Integer.parseInt(map.get("page")) - 1;
            size = Integer.parseInt(map.get("size"));
        } catch (Exception e) {
            log.error(e.getMessage());
            page = 0;
            size = 20;
        }
        String sortString = map.get("sort");
        if (null == sortString) {
            return PageRequest.of(page, size);
        } else {
            List<Sort.Order> orderList=new ArrayList<>();
            String[] columnAndDirections=sortString.split(";");
            for(int i=0;i<columnAndDirections.length;i++){
                String[] columnAndDirection = columnAndDirections[i].split(",");
                Sort.Order order=new Sort.Order(Sort.Direction.valueOf(columnAndDirection[1]),columnAndDirection[0]);
                orderList.add(order);
            }
            return PageRequest.of(page, size, Sort.by(orderList));
        }
    }

    /**
     * 查询条件扩展
     * @return
     */
    public List<Predicate> createPredicateList(){
        return new ArrayList<Predicate>();
    }

    /**
     * 获取predicateList
     * @param map
     * @return
     */
    private List<Predicate> getPredicateList(Map<String,String> map, CriteriaBuilder cb, Root<T> root){
        List<Predicate> predicateList = new ArrayList<>();
        map.forEach((k, v) -> {
            try {
                ColumnConditionBO columnConditionBO = ColumnConditionBO.parse(k);
                Predicate predicate=this.getPredicate(columnConditionBO, v, cb, root);
                if(predicate==null){
                    log.warn(k+"查询条件错误");
                }else{
                    predicateList.add(predicate);
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        return predicateList;
    }

    /**
     * 获取查询
     *
     * @param columnConditionBO 字段、条件业务类
     * @param value             查询值
     * @param criteriaBuilder   cb
     * @param root              root
     * @return 查询条件
     */
    private Predicate getPredicate(ColumnConditionBO columnConditionBO, String value, CriteriaBuilder criteriaBuilder, Root<T> root) throws NoSuchFieldException {
        //获取查询字段的类型，fieldClass为查询类中字段的类型，classType为根据类型定义的枚举类
        clazz = (Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        while (clazz!=null){
            try{
                //对于子类无法获取父类的私有属性，需要递归向上获取父类的私有属性
                Field field = ClassHelper.getDeclaredField(clazz, columnConditionBO.getColumn());
                Class fieldClass = field.getType();
                //判断fieldClass是否是枚举类
                if (!fieldClass.isEnum()) {
                    return basicPredicate(columnConditionBO, value, criteriaBuilder, root, fieldClass);
                } else {
                    return enumPredicate(columnConditionBO, value, criteriaBuilder, root, fieldClass.getEnumConstants());
                }
            }catch (Exception e){
                log.error(e.getMessage());
                clazz=clazz.getSuperclass();
            }
        }
        String noSuchFieldException="根据查询参数获取类中的域失败";
        log.warn(noSuchFieldException);
        throw new NoSuchFieldException(noSuchFieldException);
    }

    /**
     * 基础类predicate生成方法
     *
     * @param columnConditionBO 参数条件
     * @param value             查询条件值
     * @param criteriaBuilder   构建起
     * @param root              root
     * @param fieldClass        fieldClass
     * @return predicate
     */
    private Predicate basicPredicate(ColumnConditionBO columnConditionBO, String value, CriteriaBuilder criteriaBuilder, Root<T> root, Class fieldClass) {
        ClassTypeBo classType = ClassTypeBo.valueOf(fieldClass.getSimpleName());
        switch (columnConditionBO.getCondition()) {
            case lk: {
                return criteriaBuilder.like(root.get(columnConditionBO.getColumn()), value);
            }case sw: {
                return criteriaBuilder.like(root.get(columnConditionBO.getColumn()), value + "%");
            }case ew: {
                return criteriaBuilder.like(root.get(columnConditionBO.getColumn()), "%" + value);
            }case containing: {
                return criteriaBuilder.like(root.get(columnConditionBO.getColumn()), "%" + value + "%");
            }case eq: {
                return criteriaBuilder.equal(root.get(columnConditionBO.getColumn()), value);
            }case not:{
                return criteriaBuilder.notEqual(root.get(columnConditionBO.getColumn()), value);
            }case isNull:{
                return criteriaBuilder.isNull(root.get(columnConditionBO.getColumn()));
            }case isNotNull:{
                return criteriaBuilder.isNotNull(root.get(columnConditionBO.getColumn()));
            }case between: {
                String[] minAndMax = value.split(",");
                switch (classType) {
                    case LocalDateTime: {
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        return criteriaBuilder.between(root.get(columnConditionBO.getColumn()), LocalDateTime.parse(minAndMax[0], dateTimeFormatter), LocalDateTime.parse(minAndMax[1], dateTimeFormatter));
                    }case String: {
                        return criteriaBuilder.between(root.get(columnConditionBO.getColumn()), minAndMax[0], minAndMax[1]);
                    }case Integer: {
                        return criteriaBuilder.between(root.get(columnConditionBO.getColumn()), Integer.parseInt(minAndMax[0]), Integer.parseInt(minAndMax[1]));
                    }case Long: {
                        return criteriaBuilder.between(root.get(columnConditionBO.getColumn()), Long.parseLong(minAndMax[0]), Long.parseLong(minAndMax[1]));
                    }case BigDecimal: {
                        return criteriaBuilder.between(root.get(columnConditionBO.getColumn()), new BigDecimal(minAndMax[0]), new BigDecimal(minAndMax[1]));
                    }case Date:{
                        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        try {
                            return criteriaBuilder.between(root.get(columnConditionBO.getColumn()), dateTimeFormatter.parse(minAndMax[0]),dateTimeFormatter.parse(minAndMax[1]));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }default:
                        return null;
                }
            }case ge:{
                switch (classType){
                    case Date:{
                        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        try {
                            return criteriaBuilder.greaterThanOrEqualTo(root.get(columnConditionBO.getColumn()), dateTimeFormatter.parse(value));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }default:{
                        return criteriaBuilder.greaterThanOrEqualTo(root.get(columnConditionBO.getColumn()), value);
                    }
                }
            }case gt:{
                switch (classType){
                    case Date:{
                        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        try {
                            return criteriaBuilder.greaterThan(root.get(columnConditionBO.getColumn()), dateTimeFormatter.parse(value));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }default:{
                        return criteriaBuilder.greaterThanOrEqualTo(root.get(columnConditionBO.getColumn()), value);
                    }
                }
            }case le:{
                switch (classType){
                    case Date:{
                        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        try {
                            return criteriaBuilder.lessThanOrEqualTo(root.get(columnConditionBO.getColumn()), dateTimeFormatter.parse(value));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }default:{
                        return criteriaBuilder.greaterThanOrEqualTo(root.get(columnConditionBO.getColumn()), value);
                    }
                }
            }case lt:{
                switch (classType){
                    case Date:{
                        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        try {
                            return criteriaBuilder.lessThan(root.get(columnConditionBO.getColumn()), dateTimeFormatter.parse(value));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }default:{
                        return criteriaBuilder.greaterThanOrEqualTo(root.get(columnConditionBO.getColumn()), value);
                    }
                }
            }case in: {
                String[] inValues = value.split(",");
                List<String> stringList = Arrays.asList(inValues);
                switch (classType) {
                    case LocalDateTime: {
                        CriteriaBuilder.In<LocalDateTime> in = criteriaBuilder.in(root.get(columnConditionBO.getColumn()));
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        stringList.forEach(s -> in.value(LocalDateTime.parse(s, dateTimeFormatter)));
                        return in;
                    }case String: {
                        CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get(columnConditionBO.getColumn()));
                        stringList.forEach(in::value);
                        return in;
                    }case Long: {
                        CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get(columnConditionBO.getColumn()));
                        stringList.forEach(s -> in.value(Long.parseLong(s)));
                        return in;
                    }case BigDecimal: {
                        CriteriaBuilder.In<BigDecimal> in = criteriaBuilder.in(root.get(columnConditionBO.getColumn()));
                        stringList.forEach(s -> in.value(new BigDecimal(s)));
                        return in;
                    }case Integer: {
                        CriteriaBuilder.In<Integer> in = criteriaBuilder.in(root.get(columnConditionBO.getColumn()));
                        stringList.forEach(s -> in.value(Integer.parseInt(s)));
                        return in;
                    }default: {
                        return null;
                    }
                }
            }default: {
                return null;
            }
        }
    }

    /**
     * enum类predicate生成方法
     *
     * @param columnConditionBO 参数条件
     * @param value             查询值
     * @param criteriaBuilder   构建起
     * @param root              root
     * @param ys                ys
     * @return predicate
     */
    private <Y> Predicate enumPredicate(ColumnConditionBO columnConditionBO, String value, CriteriaBuilder criteriaBuilder, Root<T> root, Y[] ys) {
        //枚举类型处理
        CriteriaBuilder.In<Y> in = criteriaBuilder.in(root.get(columnConditionBO.getColumn()));
        Arrays.asList(value.split(",")).forEach(s -> Arrays.asList(ys).forEach(f -> {
            if (f.toString().equals(s)) {
                in.value(f);
            }
        }));
        return in;
    }
}
