package pl.springrest.dto;

import java.util.List;

public class ActorListDTO{
	
	private List<ActorDTO> actors;
	
	public ActorListDTO() {}
	
	public ActorListDTO(List<ActorDTO> actors) {
		this.actors = actors;
	}

	public List<ActorDTO> getActors() {
		return actors;
	}

	public void setActors(List<ActorDTO> actors) {
		this.actors = actors;
	}
	
}
