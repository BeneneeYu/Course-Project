package fudan.se.lab2.controller;

import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.SubmitRebuttalRequest;
import fudan.se.lab2.domain.Article;
import fudan.se.lab2.domain.Conference;
import fudan.se.lab2.repository.ArticleRepository;
import fudan.se.lab2.repository.ConferenceRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

@SpringBootTest
public class RebuttalControllerTest {
    @Autowired
    private RebuttalController rebuttalController;

    @Autowired
    private AuthController authController;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private MockHttpServletRequest request;

    @Test
    void rebuttal(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testD");
        loginRequest.setPassword("123456");
        String token=(String)authController.login(loginRequest).getBody().get("token");
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.addHeader("Authorization","Bearer " + token);
        Conference conference=conferenceRepository.findByFullName("testMeeting1");
        if(conference==null){
            return;
        }
        Article article=articleRepository.findByTitleAndConferenceID("article12",conference.getId());
        if(article==null){
            return;
        }
        HttpServletResponse response=new HttpServletResponse() {
            @Override
            public void addCookie(Cookie cookie) {

            }

            @Override
            public boolean containsHeader(String s) {
                return false;
            }

            @Override
            public String encodeURL(String s) {
                return null;
            }

            @Override
            public String encodeRedirectURL(String s) {
                return null;
            }

            @Override
            public String encodeUrl(String s) {
                return null;
            }

            @Override
            public String encodeRedirectUrl(String s) {
                return null;
            }

            @Override
            public void sendError(int i, String s) throws IOException {

            }

            @Override
            public void sendError(int i) throws IOException {

            }

            @Override
            public void sendRedirect(String s) throws IOException {

            }

            @Override
            public void setDateHeader(String s, long l) {

            }

            @Override
            public void addDateHeader(String s, long l) {

            }

            @Override
            public void setHeader(String s, String s1) {

            }

            @Override
            public void addHeader(String s, String s1) {

            }

            @Override
            public void setIntHeader(String s, int i) {

            }

            @Override
            public void addIntHeader(String s, int i) {

            }

            @Override
            public void setStatus(int i) {

            }

            @Override
            public void setStatus(int i, String s) {

            }

            @Override
            public int getStatus() {
                return 0;
            }

            @Override
            public String getHeader(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaders(String s) {
                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {
                return null;
            }

            @Override
            public PrintWriter getWriter() throws IOException {
                return null;
            }

            @Override
            public void setCharacterEncoding(String s) {

            }

            @Override
            public void setContentLength(int i) {

            }

            @Override
            public void setContentLengthLong(long l) {

            }

            @Override
            public void setContentType(String s) {

            }

            @Override
            public void setBufferSize(int i) {

            }

            @Override
            public int getBufferSize() {
                return 0;
            }

            @Override
            public void flushBuffer() throws IOException {

            }

            @Override
            public void resetBuffer() {

            }

            @Override
            public boolean isCommitted() {
                return false;
            }

            @Override
            public void reset() {

            }

            @Override
            public void setLocale(Locale locale) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }
        };
        SubmitRebuttalRequest submitRebuttalRequest=new SubmitRebuttalRequest();
        submitRebuttalRequest.setArticleID(article.getId());
        submitRebuttalRequest.setArticleID(article.getId());
        submitRebuttalRequest.setWords("为什么分低");
        String message=(String)rebuttalController.submitRebuttal(request,submitRebuttalRequest,response).getBody().get("message");
        Assert.isTrue(message.equals("提交失败"));
    }
}
