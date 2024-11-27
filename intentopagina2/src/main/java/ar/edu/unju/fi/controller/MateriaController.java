package ar.edu.unju.fi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.edu.unju.fi.DTO.DocenteDTO;
import ar.edu.unju.fi.DTO.MateriaDTO;
import ar.edu.unju.fi.service.DocenteService;
import ar.edu.unju.fi.service.MateriaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/materia")
public class MateriaController {

    @Autowired
    @Qualifier("materiaServiceImp")
    MateriaService materiaService;

    @Autowired
    @Qualifier("docenteServiceImp")
    DocenteService docenteService;
    
    @Autowired
    MateriaDTO nuevaMateriaDTO;
  
    @GetMapping("/listado")
    public String getMateria(Model model) {
        try {
        	List<MateriaDTO> materias = materiaService.MostrarMateria();
        	System.out.println("Materias en el modelo: " + materias); 
            model.addAttribute("materia", materiaService.MostrarMateria());
           
        } catch (Exception e) {
           
            model.addAttribute("error", "Ocurrió un error al obtener la lista de materias.");
        }
        return "ListadoDeMateria";
    }


    @GetMapping("/nuevo")
    public String getVistaNuevaCarrera(Model model) {
        boolean edicion = false; 
        model.addAttribute("nuevaMateria", nuevaMateriaDTO);
        model.addAttribute("edicion", edicion);
    
        List<DocenteDTO> docentes = docenteService.MostrarDocente();
        model.addAttribute("docentes", docentes);
        
        return "formMateria";
    }

  
    @PostMapping("/guardar")
    public String guardarMateria(@Valid @ModelAttribute("nuevaMateria") MateriaDTO materiaDTO,BindingResult resultado, Model model) {
    	if (resultado.hasErrors()) {
    		model.addAttribute("nuevaMateria", materiaDTO);
            model.addAttribute("edicion", false);
         
               List<DocenteDTO> docentes = docenteService.MostrarDocente();
               model.addAttribute("docentes", docentes);
            return "formMateria"; 
		}
    	else {
			 try { 
            materiaService.save(materiaDTO);
	        } catch (Exception e) {
	          
	        	model.addAttribute("Error Al cargar el docente", e.getMessage());
	            return "redirect:/materia/nuevo?error=true";
	        }
	        return "redirect:/materia/listado";
		}
        
    }

 
    @GetMapping("/modificarMateria/{codigo}")
    public String getModificarMateriaPague(Model model, @PathVariable(value = "codigo") String codigo) {
        try {
            MateriaDTO materiaEncontradoDTO = materiaService.findByCodigo(codigo);
            boolean edicion = true;
            model.addAttribute("nuevaMateria", materiaEncontradoDTO);
            model.addAttribute("edicion", edicion);
          
               List<DocenteDTO> docentes = docenteService.MostrarDocente();
               model.addAttribute("docentes", docentes);
        } catch (Exception e) {
          
            return "redirect:/materia/listado?error=true";
        }
        return "formMateria";
    }


    @PostMapping("/modificar")
    public String modificarMateria(@Valid @ModelAttribute("nuevaMateria") MateriaDTO materiaDTO,BindingResult resultado, Model model) {
    	if (resultado.hasErrors()) {
    		model.addAttribute("nuevaMateria", materiaDTO);
            return "formMateria";
		}
    	else {
			 try {
            materiaService.edit(materiaDTO);
	        } catch (Exception e) {
	    
	            return "redirect:/materia/modificar/" + materiaDTO.getCodigo() + "?error=true";
	        }
	        return "redirect:/materia/listado";
		}
       
    }


    @GetMapping("/borrarMateria/{codigo}")
    public String eliminarMateria(@PathVariable(value = "codigo") String codigo) {
        try {
            materiaService.deleteByCodigo(codigo);
        } catch (Exception e) {
   
            return "redirect:/materia/listado?error=true";
        }
        return "redirect:/materia/listado";
    }
}
