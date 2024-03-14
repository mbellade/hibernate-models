/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright: Red Hat Inc. and Hibernate Authors
 */

package org.hibernate.models.internal;

import java.util.List;

import org.hibernate.models.spi.ClassDetails;
import org.hibernate.models.spi.ParameterizedTypeDetails;
import org.hibernate.models.spi.TypeDetails;
import org.hibernate.models.spi.TypeVariableDetails;
import org.hibernate.models.spi.TypeVariableScope;

/**
 * @author Steve Ebersole
 */
public class ParameterizedTypeDetailsImpl implements ParameterizedTypeDetails {
	private final ClassDetails genericClassDetails;
	private final List<TypeVariableDetails> typeVariables;
	private final List<TypeDetails> arguments;
	private TypeVariableScope owner;

	public ParameterizedTypeDetailsImpl(
			ClassDetails genericClassDetails,
			List<TypeVariableDetails> typeVariables,
			List<TypeDetails> arguments,
			TypeVariableScope owner) {
		this.genericClassDetails = genericClassDetails;
		this.typeVariables = typeVariables;
		this.arguments = arguments;
		this.owner = owner;
	}

	@Override
	public ClassDetails getRawClassDetails() {
		return genericClassDetails;
	}

	public List<TypeVariableDetails> getTypeVariables() {
		return typeVariables;
	}

	@Override
	public List<TypeDetails> getArguments() {
		return arguments;
	}

	@Override
	public TypeVariableScope getOwner() {
		return owner;
	}

	@Override
	public TypeDetails resolveTypeVariable(TypeVariableDetails typeVariable, ClassDetails declaringType) {
		final List<TypeVariableDetails> typeParameters = genericClassDetails.getTypeParameters();
		assert typeParameters.size() == arguments.size();

		for ( int i = 0; i < typeParameters.size(); i++ ) {
			if ( typeParameters.get( i ).getIdentifier().equals( typeVariable.getIdentifier() ) ) {
				return arguments.get( i );
			}
		}

		return null;
	}
}
