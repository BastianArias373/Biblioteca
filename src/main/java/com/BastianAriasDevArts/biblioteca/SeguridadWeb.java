package com.BastianAriasDevArts.biblioteca;

import com.BastianAriasDevArts.biblioteca.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter {
    
    @Autowired
    public UsuarioServicio usuarioServicio;
    
    //este metodo es para encriptar las contraseñas
    //para que no queden guardadas planas en la DB
    @Autowired
    public void configureGlobal(
            AuthenticationManagerBuilder auth) 
            throws Exception{
        auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
    }
    
    
    //este metodod es para que los recursos que estuviesen en esas carpetas 
    //sean accesibles para cualquier usuario, 
    //sin la necesidad de esta logueado, registrado ni con permisos especiales
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()                                //autorizar solicitudes
                    .antMatchers("/admin/*").hasRole("ADMIN")       //solo entran a esta class los ADMIN
                    .antMatchers("/css/*","/js/*","/img/*","/**")   //carpetas accesibles
                    .permitAll()                                    //permitir todo
                .and()
                .formLogin()                            //config para login
                    .loginPage("/login")                //URL donde esta el form del login
                    .loginProcessingUrl("/logincheck")  //URL donde SpringSecrity autentica
                    .usernameParameter("email")         //config credencial email
                    .passwordParameter("password")      //config credencial password
                    .defaultSuccessUrl("/inicio")       //URL donde nos envia exitosamente!
                    .permitAll()                        //permitir todo
                .and()
                .logout()                   //config salida del sistema
                    .logoutUrl("/logout")   //URL para el cierre de sesion
                    .logoutSuccessUrl("/")  //URL donde nos envia exitosamente!
                    .permitAll()            //permitir todo
                .and()
                .csrf()
                    .disable();           
    }
    
    
    
}
