package com.softwaretest.demo.Config;

import org.springframework.context.annotation.Configuration;

@Configuration

public class SecurityConfig  {
    /*
    @Override
    public void configure(HttpSecurity http) throws Exception{
        //TODO: configure our http security
        http.authorizeRequests().
                antMatchers("*").permitAll().
                and().
                csrf().disable();
    }

    @Override
    public void configure(WebSecurity web){
        web.ignoring().antMatchers("/hello");
        web.ignoring().antMatchers("/data/user/validate-code");
        web.ignoring().antMatchers("/data/team");
        web.ignoring().antMatchers("/data/activity");
    }

     */
}
