package com.projet.todos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private Object data;
    private int responseCode;
    private String status;

    public Response(int responseCode, String status) {
        this.responseCode = responseCode;
        this.status = status;
    }
}
