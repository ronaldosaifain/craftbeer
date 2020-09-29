package com.beerhouse.resource;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.beerhouse.domain.Beer;
import com.beerhouse.service.BeerService;

@RestController
@RequestMapping(value = "/beers")
public class BeerResource {

	@Autowired
	BeerService service;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Beer>> findAll() {

		List<Beer> listBeer = service.findAll();

		return ResponseEntity.ok(listBeer);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Beer> find(@PathVariable Integer id) {

		Beer beer = service.find(id);

		return ResponseEntity.ok(beer);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Beer beer) {

		Beer obj = service.save(beer);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody  Beer beer, @PathVariable Integer id) {
		beer.setId(id);
		service.update(beer);

		return ResponseEntity.ok().build();
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<Void> partialUpdate(@RequestBody Beer beer, @PathVariable Integer id) {
		beer.setId(id);
		service.update(beer);
		return ResponseEntity.ok().build();
	}
	

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {

		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
