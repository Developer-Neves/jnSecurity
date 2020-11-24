package com.jdnevesti.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jdnevesti.security.domain.Medico;
import com.jdnevesti.security.domain.Usuario;
import com.jdnevesti.security.service.MedicoService;
import com.jdnevesti.security.service.UsuarioService;

@Controller
@RequestMapping("medicos")
public class MedicoController {

	@Autowired
	private MedicoService medicoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	// Abrir página de dados pessoais de medicos pelo medico
	@GetMapping({"/dados"})
	public String abrirPorMedico(Medico medico, ModelMap model, @AuthenticationPrincipal User user) {
		if(medico.hasNotId()) {
			medico = medicoService.buscarPorEmail(user.getUsername());
			model.addAttribute("medico", medico);
		}
		return "medico/cadastro";
	}
	
	// salvar medico
	@PostMapping({"/salvar"})
	public String salvar(Medico medico, RedirectAttributes attr, @AuthenticationPrincipal User user) {
		if(medico.hasNotId() && medico.getUsuario().hasNotId()) {
			Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
			medico.setUsuario(usuario);
		}
		medicoService.salvar(medico);
		attr.addFlashAttribute("sucesso", "Cadastro realizado com sucesso!");
		attr.addFlashAttribute("medico", medico);
		return "redirect:/medicos/dados";
	}
	
	// editar medico
	@PostMapping({"/editar"})
	public String editar(Medico medico, RedirectAttributes attr) {
		medicoService.editar(medico);
		attr.addFlashAttribute("sucesso", "Atualização realizada com sucesso!");
		attr.addFlashAttribute("medico", medico);
		return "redirect:/medicos/dados";
	}
}
