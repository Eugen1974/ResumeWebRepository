package com.urise.webapp.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = TextSection.class), 
				@Type(value = ListSection.class),
				@Type(value = OrganizationSection.class),
				@Type(value = Organization.class),
				@Type(value = Link.class),
				@Type(value = Organization.Position.class)})
public abstract class Section implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	protected abstract Section clone();
}
