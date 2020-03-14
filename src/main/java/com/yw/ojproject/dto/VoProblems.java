package com.yw.ojproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @program: ojproject
*
* @description: problems 情况视图
*
* @author: YW
*
* @create: 2020-03-11 18:47
**/
@Data
@NoArgsConstructor
public class VoProblems implements Serializable {

    public VoProblems(Integer id, Integer score, String status)
    {
        this.id = id;
        this.score = score;
        this.status = status;
    }
    private Integer id;
    private Integer score;
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VoProblems)) {
            return false;
        }

        VoProblems that = (VoProblems) o;

        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public String toString()
    {
        return "{" +
                "\"status\":\"" + status + "\"," +
                "\"score \":\"" + score + "\"," +
                "\"_id\":\"" +  id.toString() + "\"" +
                "}";
    }
}

