package pl.springrest.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long Id;
	
	private String uuid = UUID.randomUUID().toString();
	
	public Long getId() {
		return Id;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(uuid);
	}
	
	@Override
	public boolean equals(Object that) {
		return this == that || that instanceof BaseEntity 
				&& Objects.equals(uuid, ((BaseEntity)that).uuid);
	}
}
