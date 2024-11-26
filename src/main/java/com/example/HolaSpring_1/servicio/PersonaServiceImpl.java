package com.example.HolaSpring_1.servicio;

import com.example.HolaSpring_1.dao.PersonaDao;
import com.example.HolaSpring_1.domain.Persona;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonaServiceImpl implements PersonaService{
    
    @Autowired
    private PersonaDao personaDao;

    @Override
    @Transactional(readOnly = true)
    public List<Persona> listarPersonas() {
        return (List<Persona>) personaDao.findAll();
    }

    @Override
    @Transactional
    public void guardar(Persona persona) {
        personaDao.save(persona);
    }

    @Override
    @Transactional
    public void eliminar(Persona persona) {
        personaDao.delete(persona);
    }

    @Override
    @Transactional(readOnly = true)
    public Persona encontrarPersona(Persona persona) {
//        Optional<Persona> personaOptional = personaDao.findById(persona.getIdPersona());
//        if(personaOptional.isPresent()){
//            Persona personaEncontrada = personaOptional.get();
//            return personaEncontrada;
//        }else{
//            return null;
//        }
        
        //Esto de abajo es masomenos lo que hace lo de arriba
        return personaDao.findById(persona.getIdPersona()).orElse(null);
    }
    
}
