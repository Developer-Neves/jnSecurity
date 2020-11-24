package com.jdnevesti.security.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jdnevesti.security.domain.Medico;
import com.jdnevesti.security.domain.Perfil;
import com.jdnevesti.security.domain.PerfilTipo;
import com.jdnevesti.security.domain.Usuario;
import com.jdnevesti.security.service.MedicoService;
import com.jdnevesti.security.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {

	@Autowired
	private UsuarioService service;
	
	@Autowired
	private MedicoService medicoService;
	
	// Abrir página de dados pessoais de medicos pelo MEDICO
	@GetMapping("/novo/cadastro/usuario")
	public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {		
		return "usuario/cadastro";
	}
	
	// Abrir lista de usuários
	@GetMapping("/lista")
	public String lista() {		
		return "usuario/lista";
	}
	
	// listar usuários na datatables
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDatatebles(HttpServletRequest request) {
		
		return ResponseEntity.ok(service.buscarTodos(request));
	}
	
	// salvar cadastro de usuarios por administrador
	@PostMapping("/cadastro/salvar")
	public String salvarUsuarios(Usuario usuario, RedirectAttributes attr) {
		
		List<Perfil> perfis = usuario.getPerfis();
		if(perfis.size() > 2 || 
		   perfis.containsAll(Arrays.asList( new Perfil(1L), new Perfil(3L))) || 
		   perfis.containsAll(Arrays.asList( new Perfil(2L), new Perfil(3L))))	{
		  attr.addFlashAttribute("falha", "Paciente não pode ser Admin e/ou Médico.");
		  attr.addFlashAttribute("usuario", usuario);
		} else {
			try {
				service.salvarUsusario(usuario);
				attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
			} catch(DataIntegrityViolationException e) {
				attr.addFlashAttribute("falha", "Cadastro não realizado, email já existente.");
			}
		}		
		return "redirect:/u/novo/cadastro/usuario";
	}
	
	// pre editar credenciais do usuário
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEdittarCredenciais(@PathVariable("id") Long id) {
		
		return new ModelAndView("usuario/cadastro", "usuario", service.buscarPorId(id));
	}
	
	// pre editar cadastro do usuário
	@GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
	public ModelAndView preEdittarCadastroPessoais(@PathVariable("id") Long usuarioId, @PathVariable("perfis") Long[] perfisId) {
		Usuario user = service.buscarPorIdEPerfis(usuarioId, perfisId);
		
		if(user.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
		  !user.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
			return new ModelAndView("usuario/cadastro", "usuario", user);
			
		} else if(user.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
			Medico medico = medicoService.buscarPorUsuarioId(usuarioId);	
			return medico.hasNotId() 
					? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
					: new ModelAndView("medico/cadastro", "medico", medico);
			
		} else if(user.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
			ModelAndView model = new ModelAndView("error");
			model.addObject("status", 403);
			model.addObject("error", "Área Restrita.");
			model.addObject("message", "Os dados de pacientes são restritos a ele.");
			return model;
		}
		
		return new ModelAndView("redirect:/u/lista");
	}
	
}
