package com.curso.spring.services;

import com.curso.spring.domain.Cidade;
import com.curso.spring.domain.Cliente;
import com.curso.spring.domain.Endereco;
import com.curso.spring.dto.ClienteDTO;
import com.curso.spring.dto.ClienteNewDTO;
import com.curso.spring.enums.PerfilCliente;
import com.curso.spring.enums.TipoCliente;
import com.curso.spring.repositories.CidadeRepository;
import com.curso.spring.repositories.ClienteRepository;
import com.curso.spring.repositories.EnderecoRepository;
import com.curso.spring.security.UserSS;
import com.curso.spring.services.exceptions.AuthorizationException;
import com.curso.spring.services.exceptions.DataIntegrityException;
import com.curso.spring.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public Cliente find(Integer id) {

		UserSS user = UserService.authenticated();
		if (user==null || !user.hasRole(PerfilCliente.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}

		Cliente obj = repo.findOne(id);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
					+ ", Tipo: " + Cliente.class.getName());
		}
		return obj;
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Cliente update(Cliente obj){
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.save(obj.getEnderecos());
		return obj;
	}

	public void delete(Integer id){
		find(id);
		try {
			repo.delete(id);
		}
		catch (DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possível excluir Cliente porque há pedidos relacionados");
		}
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}


	public Cliente fromDTO(ClienteDTO objDto){
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}

	private void updateData(Cliente newObj, Cliente obj){
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(),
				TipoCliente.toEnum(objDto.getTipo()), bCryptPasswordEncoder.encode(objDto.getSenha()));
		Cidade cid = cidadeRepository.findOne(objDto.getCidadeId());
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(),
				objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if (objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if (objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		return cli;
	}


}
