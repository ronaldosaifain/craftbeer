package com.beerhouse.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beerhouse.domain.Beer;
import com.beerhouse.exception.ObjectNotFoundException;
import com.beerhouse.repository.BeerRepository;


@Service
public class BeerService {

	@Autowired
	BeerRepository beerRepository;
	

	public List<Beer> findAll() {
		return beerRepository.findAll();
	}

	public Beer find(Integer id) {
		Optional<Beer>  beer = Optional.ofNullable(beerRepository.findOne(id));
		return beer.orElseThrow( () ->
		 new ObjectNotFoundException(
				 "Objeto não encontrado! Id: " + id + ", Tipo: " + Beer.class.getName())); 
	}

	public Beer save(Beer beer) {
		return beerRepository.save(beer);
		
	}
	
	public Beer update(Beer beer) {
		Beer newBeer = find(beer.getId());
		updateData(newBeer, beer);
		return beerRepository.save(newBeer);
	}

	public void delete(Integer id) {
		Beer newBeer = find(id);
		beerRepository.delete(newBeer.getId());
	}
	
	private void updateData(Beer newBeer, Beer beer) {
		if(beer.getName() != null)
		newBeer.setName(beer.getName());
		if(beer.getIngredients() != null)
		newBeer.setIngredients(beer.getIngredients());	
		if(beer.getAlcoholContent() != null)
		newBeer.setAlcoholContent(beer.getAlcoholContent());
		if(beer.getPrice() != null)
		newBeer.setPrice(beer.getPrice());
		if(beer.getCategory() != null)
		newBeer.setCategory(beer.getCategory());	

	}

}
