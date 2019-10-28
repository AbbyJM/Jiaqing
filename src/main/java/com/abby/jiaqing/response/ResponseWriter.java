package com.abby.jiaqing.response;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

public class ResponseWriter {
    public static void writeToResponseThenClose(HttpServletResponse response,String message) throws IOException {
        PrintWriter writer=response.getWriter();
        response.setContentType("application/json;");
        response.setCharacterEncoding("UTF-8");
        writer.write(message);
        writer.flush();
        writer.close();
    }
}
