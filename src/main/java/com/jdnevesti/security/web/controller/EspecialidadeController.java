package com.jdnevesti.security.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jdnevesti.security.domain.Especialidade;
import com.jdnevesti.security.service.EspecialidadeService;

@Controller
@RequestMapping("especialidades")
public class EspecialidadeController {

	@Autowired
	private EspecialidadeService service;
	
	// Abrir página de especialidades
	@GetMapping({"", "/"})
	public String abrir(Especialidade especialidade) {
		
		return "especialidade/especialidade";
	}
	
	// Salvar especialidade
	@PostMapping("/salvar")
	public String salvar(Especialidade especialidade, RedirectAttributes attr) {
		service.salvar(especialidade);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
		return "redirect:/especialidades";
	}
	
	// Listar especialidades
	@GetMapping("/datatables/server")
	public ResponseEntity<?> getEspecialidades(HttpServletRequest request) {
		
		return ResponseEntity.ok(service.buscarEspecialidades(request));
	}
	
	// Pre-editar especialidade
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("especialidade", service.buscarPorId(id));
		return "especialidade/especialidade";
	}
	
	// Excluir especialidade
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable Long id, RedirectAttributes attr) {
		service.remover(id);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
		return "redirect:/especialidades";
	}
	
}
