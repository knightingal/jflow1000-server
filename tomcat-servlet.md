```java
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The simplest possible servlet.
 *
 * @author James Duncan Davidson
 */

public class ChunkExample extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        final  HttpServletResponse responseLocal = response;
        ServletInputStream inputStream = request.getInputStream();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                responseLocal.setStatus(200);
                responseLocal.setContentType("text");
                responseLocal.setCharacterEncoding("UTF-8");

                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(10 * 1000);
                        responseLocal.getOutputStream().write("respcontent".getBytes());
                        responseLocal.getOutputStream().flush();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
        thread.start();
        byte[] buff = new byte[1024];
        int totalLength = 0;
        int contentLen = request.getContentLength();
        try {
            do {
                int readLen = inputStream.read(buff);
                totalLength += readLen;
                if (readLen <= 0) {
                    throw new Exception("read EOF");
                }
                log(new String(buff, 0, readLen));
//                response.getOutputStream().write(buff, 0, readLen);
//                response.getOutputStream().flush();

            } while (totalLength < contentLen);
//            ResourceBundle rb =
//                ResourceBundle.getBundle("LocalStrings",request.getLocale());
//            response.setContentType("text/html");
//            response.setCharacterEncoding("UTF-8");
//            PrintWriter out = response.getWriter();
//
//            out.println("<!DOCTYPE html><html>");
//            out.println("<head>");
//            out.println("<meta charset=\"UTF-8\" />");
//
//            String title = rb.getString("helloworld.title");
//
//            out.println("<title>" + title + "</title>");
//            out.println("</head>");
//            out.println("<body bgcolor=\"white\">");
//
//            // note that all links are created to be relative. this
//            // ensures that we can move the web application that this
//            // servlet belongs to a different place in the url
//            // tree and not have any harmful side effects.
//
//            // XXX
//            // making these absolute till we work out the
//            // addition of a PathInfo issue
//
//            out.println("<a href=\"../helloworld.html\">");
//            out.println("<img src=\"../images/code.gif\" height=24 " +
//                "width=24 align=right border=0 alt=\"view code\"></a>");
//            out.println("<a href=\"../index.html\">");
//            out.println("<img src=\"../images/return.gif\" height=24 " +
//                "width=24 align=right border=0 alt=\"return\"></a>");
//            out.println("<h1>" + title + "</h1>");
//            out.println("</body>");
//            out.println("</html>");

        } catch (Exception e) {
            log(e.getMessage());
//            response.setStatus(500);
        }
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        ResourceBundle rb =
            ResourceBundle.getBundle("LocalStrings",request.getLocale());
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html>");
        out.println("<head>");
        out.println("<meta charset=\"UTF-8\" />");

        String title = rb.getString("helloworld.title");

        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");

        // note that all links are created to be relative. this
        // ensures that we can move the web application that this
        // servlet belongs to a different place in the url
        // tree and not have any harmful side effects.

        // XXX
        // making these absolute till we work out the
        // addition of a PathInfo issue

        out.println("<a href=\"../helloworld.html\">");
        out.println("<img src=\"../images/code.gif\" height=24 " +
                    "width=24 align=right border=0 alt=\"view code\"></a>");
        out.println("<a href=\"../index.html\">");
        out.println("<img src=\"../images/return.gif\" height=24 " +
                    "width=24 align=right border=0 alt=\"return\"></a>");
        out.println("<h1>" + title + "</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}
```