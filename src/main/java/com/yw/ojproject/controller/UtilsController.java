package com.yw.ojproject.controller;

import com.yw.ojproject.dto.ReturnData;
import com.yw.ojproject.utils.CVSUtils;
import com.yw.ojproject.utils.RandUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-04-25 16:06
**/
@RestController
@RequestMapping("/api")
public class UtilsController {

    @PostMapping("/admin/generate_user")
    @RequiresRoles(value = {"admin", "superadmin"}, logical = Logical.OR)
    public ReturnData downloadAllUserRoleCSV(@RequestBody Map<String, String> params, HttpServletResponse response) throws IOException {
        String[] head = {"user", "password"};
        List<CVSUtils.CSVEntity> values = RandUtils.makeRandomUser(params.get("prefix"), params.get("suffix"), Integer.valueOf(params.get("number_from")),
                Integer.valueOf(params.get("number_to")), Integer.valueOf(params.get("password_length")));
        String fileName = "users";

        File file = CVSUtils.makeTempCSV(fileName, head, values);
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName +".csv");
        CVSUtils.downloadFile(response, file);
        return null;
    }

}
