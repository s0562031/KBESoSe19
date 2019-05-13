package de.htw.ai.kbe.songsServlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple SimpleServlet.
 */

// TODO: momentan testen wir nur die Methoden der SongsHelper in Verbindung mit den HTTP-Requests des Servlets, die Methoden an sich sind damit von den Tests nicht abgedeckt

public class AppTest
{
	/*
    private App servlet;
    private MockServletConfig config;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setUp() throws ServletException {
        servlet = new App();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        response.setCharacterEncoding("UTF-8"); // very important, otherwise the strings can't be equal
        config = new MockServletConfig();
        servlet.init(config); //throws ServletException
    }

    @Test
    public void doGet_noParameters() throws IOException {
        request.addHeader("Accept", "application/json");
        servlet.doGet(request, response);
        assertEquals(request.getHeader("Accept"), response.getContentType());
    }
    */

}

   