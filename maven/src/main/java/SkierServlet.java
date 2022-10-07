import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@WebServlet(name = "SkierServlet", value = "/skiers/*")
public class SkierServlet extends HttpServlet {
    private int count = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");
        String urlPath = req.getPathInfo();

        // check we have a URL!
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            res.getWriter().write("missing paramterers");
            return;
        }

        String[] urlParts = urlPath.split("/");
        // and now validate url path and return the response status code
        // (and maybe also some value if input is valid)
        boolean validUrl = isUrlValid(urlParts);
        System.out.println("valid url: "+ validUrl);

        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            // do any sophisticated processing with urlParts which contains all the url params
            // TODO: process url params in `urlParts`

            res.getWriter().write("It works!");
        }
    }

    private boolean isUrlValid(String[] urlPath) {
        // TODO: validate the request url path according to the API spec
        // urlPath  = "/1/seasons/2022/day/1/skier/123"
        // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
        for (int i = 1; i < urlPath.length; i++) {
            // resort id
            if (i == 1) {
                int resortId = Integer.valueOf(urlPath[i]);
                if (resortId < 1 || resortId > 10 ) {
                    return false;
                }
            } else if (i == 2) {
                if (!urlPath[i].equals("seasons")) {
                    return false;
                }
            } else if (i == 3) {
                if (!urlPath[i].equals("2022")) {
                    return false;
                }
            } else if (i == 4) {
                if (!urlPath[i].equals("days")) {
                    return false;
                }
            } else if (i == 5) {
                if (!urlPath[i].equals("1")) {
                    return false;
                }
            } else if (i == 6) {
                if (!urlPath[i].equals("skiers")) {
                    return false;
                }

            } else if (i == 7) {
                // skier id
                int time = Integer.valueOf(urlPath[i]);
                if (time < 1 || time > 100000) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String urlPath = req.getPathInfo();
        res.setContentType("text/plain");

        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("url is empty");
            return;
        }
        String[] urlParts = urlPath.split("/");
        if (!isUrlValid(urlParts)) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("url is not valid");
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("Successfully write a new lift for: " + urlParts[7]);
        }
    }
}
