package fudan.se.lab2.security;

import fudan.se.lab2.security.jwt.JwtRequestFilter;
import fudan.se.lab2.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.util.Arrays;

/**
 * @author LBW
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtUserDetailsService userDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    public SecurityConfig(JwtUserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // TODO: Configure your auth here. Remember to read the JavaDoc carefully.
        /*
         * 指定用户认证时，默认从哪里获取认证用户信息
         */
        auth.userDetailsService(userDetailsService);

//        auth.userDetailsService(userDetailsService);
//
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin").password(passwordEncoder.encode("password")).authorities("administrator","user");
//                .and()
//                .withUser("admin")
//                .password(passwordEncoder.encode("password"))
//                .roles("ADMIN","USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: you need to configure your http security. Remember to read the JavaDoc carefully.
        http.authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .headers().frameOptions().disable()
                .and()
                .logout().logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);



//        http.authorizeRequests()
//                .antMatchers("/user").hasAnyRole("administrator","user")//个人首页只允许拥有MENBER,SUPER_ADMIN角色的用户访问
//                .antMatchers("/admin").hasAnyAuthority("administrator")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/")//这里程序默认路径就是登陆页面，允许所有人进行登陆
//                    .loginProcessingUrl("/login")//登陆提交的处理url
//                    .failureForwardUrl("/login")//登陆失败进行转发，这里回到登陆页面，参数error可以告知登陆状态
//                    .defaultSuccessUrl("/user")//登陆成功的url，这里去到个人首页
//                .permitAll()
//                .and()
//                .logout()
//                .logoutUrl("/logout").permitAll()
//                    .and()
//                .rememberMe();
//
        // We dont't need CSRF for this project.
                // Make sure we use stateless session; session won't be used to store user's state.


//      Here we use JWT(Json Web Token) to authenticate the user.
//      You need to write your code in the class 'JwtRequestFilter' to make it works.

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Hint: Now you can view h2-console page at `http://IP-Address:<port>/h2-console` without authentication.
        web.ignoring().antMatchers("/h2-console/**");
        web.ignoring().antMatchers("/login");
        web.ignoring().antMatchers("/welcome");
        web.ignoring().antMatchers("/register");
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("/**/ApplyConference/**");
        web.ignoring().antMatchers("/ApplyConference/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/js/web/viewer.html?file=/preview");
        web.ignoring().antMatchers("/**/Rebuttal");
        web.ignoring().antMatchers("/Rebuttal/**");
        web.ignoring().antMatchers("/**/submitRebuttal");
        web.ignoring().antMatchers("/submitRebuttal/**");


    }

//    @Bean
//    public PasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
