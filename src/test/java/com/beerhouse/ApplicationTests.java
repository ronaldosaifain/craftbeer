package com.beerhouse;


import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import com.beerhouse.domain.Beer;
import com.beerhouse.exception.ObjectNotFoundException;
import com.beerhouse.service.BeerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackageClasses = Application.class)
@AutoConfigureMockMvc
public class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private BeerService service;

	@Test
	public void shouldReturnSuccess_WhenInsertBeer() throws JsonProcessingException, Exception {

		Beer beer = new Beer(1, "Blue Moon", "Trigo e malte de cevada", "5,4%", 110.0, "Witbier");

		mockMvc.perform(post("/beers").contentType("application/json").content(objectMapper.writeValueAsString(beer)))
				.andExpect(status().isCreated());
		
		Beer beerReturn = service.find(beer.getId());
		
		Assertions.assertEquals(beerReturn.getId(), beer.getId());
		Assertions.assertEquals(beerReturn.getName(), beer.getName());
		
	}

	@Test
	public void shouldReturnSuccess_WhenFindBeer() throws Exception {

		Beer beer = new Beer(1, "Louvada", "Cascas de ponkan e coentro", "4,3%", 21.9, "Witbier");
		service.save(beer);
		
		mockMvc.perform(get("/beers/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(beer.getName())));
	}
	
	
	@Test
	public void shouldReturnSuccess_WhenUpdateBeer() throws JsonProcessingException, Exception {

		Beer beer = new Beer(null,"DUM Grand", "Trigo", "8,8%", 23.9, "Double Witbier");
		Beer beerUpdated = service.save(beer);
		
		beerUpdated.setName("Brabandere Wittekerke");
		beerUpdated.setIngredients("Casca de laranja e semente de coentro");
		beerUpdated.setAlcoholContent("8,5%");
		beerUpdated.setPrice(20.0);;
		beerUpdated.setCategory("Witbier");
		
		mockMvc.perform(put("/beers/{id}", beerUpdated.getId()).contentType("application/json").content(objectMapper.writeValueAsString(beerUpdated)))
				.andExpect(status().isOk());

		Beer beerReturn = service.find(beerUpdated.getId());

		Assertions.assertEquals(beerReturn.getId(), beerUpdated.getId());
		Assertions.assertEquals(beerReturn.getName(), beerUpdated.getName());

	}
	

	@Test
	public void shouldReturnSuccess_WhenDeleteBeer() throws Exception {
		mockMvc.perform(delete("/beers/{id}", 1)).andExpect(status().isNoContent());
			
	}
	
	
	@Test(expected = ObjectNotFoundException.class)
	public void shouldReturnException_WhenBeer() throws Exception {
		try {
			mockMvc.perform(get("/beers/{id}", 10).contentType(MediaType.APPLICATION_JSON));	
			
		} catch (NestedServletException e) {
		   	
			throw (Exception) e.getCause();
		}
	}
	
	
}