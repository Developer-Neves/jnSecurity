package com.jdnevesti.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jdnevesti.security.domain.Paciente;
import com.jdnevesti.security.domain.Usuario;
import com.jdnevesti.security.service.PacienteService;
import com.jdnevesti.security.service.UsuarioService;

@Controller
@RequestMapping("pacientes")
public class PacienteController {

	@Autowired
	private PacienteService pacienteService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	// Abrir página de dados pessoais do paciente
	@GetMapping("/dados")
	public String cadastrar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
		paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
		if(paciente.hasNotId()) {
			paciente.setUsuario(new Usuario(user.getUsername()));
		}
		model.addAttribute("paciente", paciente);
		return "/paciente/cadastro";
	}
	
	// Salvar o form de dados pessoais do paciente com verificação de senha
	@PostMapping("/salvar")
	public String salvar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		if(UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usuario.getSenha())) {
			paciente.setUsuario(usuario);
			pacienteService.salvar(paciente);
			model.addAttribute("sucesso", "Seus dados foram inseridos com sucesso!");
		} else {
			model.addAttribute("falha", "Sua senha não confere, tente novamente.");
		}
		return "paciente/cadastro";
	}
	
	// Editar os dados pessoais do paciente com verificação de senha
	@PostMapping("/editar")
	public String editar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		if(UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usuario.getSenha())) {
			pacienteService.editar(paciente);
			model.addAttribute("sucesso", "Seus dados foram alterados com sucesso!");
		} else {
			model.addAttribute("falha", "Sua senha não confere, tente novamente.");
		}
		return "paciente/cadastro";
	}
}
