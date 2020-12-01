package com.jdnevesti.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jdnevesti.security.domain.PerfilTipo;
import com.jdnevesti.security.service.UsuarioService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
	private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();
	
	@Autowired
	private UsuarioService service;

	@Override
	protected void configure(HttpSecurity http) throws Exception {	
		// Acesso públicos liberados
		http.authorizeRequests()
		.antMatchers("/", "/home").permitAll()
		.antMatchers("/webjars/**", "/css/**", "/js/**", "/image/**").permitAll()
		
		// Acessos privados Admin
		.antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(MEDICO, PACIENTE)
		.antMatchers("/u/**").hasAuthority(ADMIN)
				
		// Acessos privados especialidades
		.antMatchers("/especialidades/datatables/server/medico/*").hasAnyAuthority(ADMIN, MEDICO)
		.antMatchers("/especialidades/titulo").hasAnyAuthority(ADMIN, MEDICO)
		.antMatchers("/especialidades/**").hasAuthority(ADMIN)
		
		// Acessos privados médicos
		.antMatchers("/medicos/dados", "/medicos/salvar", "/medicos/editar").hasAnyAuthority(ADMIN, MEDICO)
		.antMatchers("/medicos/**").hasAuthority(MEDICO)
		
		// Acessos privados pacientes
		.antMatchers("/pacientes/**").hasAuthority(PACIENTE)
		
		.anyRequest().authenticated()		
		.and()
		    .formLogin()
		    .loginPage("/login")
		    .defaultSuccessUrl("/", true)
		    .failureUrl("/login-error")
		    .permitAll()
		 .and()
		  	.logout()
		  	.logoutSuccessUrl("/")
		 .and()
		 	.exceptionHandling()
		 	.accessDeniedPage("/acesso-negado")
		    ;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}

	
}
