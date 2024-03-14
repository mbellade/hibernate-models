/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright: Red Hat Inc. and Hibernate Authors
 */

package org.hibernate.models.spi;

import java.util.List;

/**
 * Models a resolved type parameter or type argument.
 * <p/>
 * The {@linkplain #getName() name} of the type variable corresponds to the raw type name,
 * which is the first upper bound. The {@linkplain #getIdentifier() identifier}  is the name
 * of the type variable as present in the source code.
 * For class-level type variables, {@linkplain #getDeclaringType() declaring type} corresponds
 * to the class that defined this type variable.
 * <p/>
 * For example:
 * <pre class="brush:java">
 * T extends Number
 * </pre>
 * In this case, the identifier is {@code T}, while the name is {@code java.lang.Number}.
 *
 * @see java.lang.reflect.TypeVariable
 * @see org.jboss.jandex.TypeVariable
 *
 * @author Steve Ebersole
 */
public interface TypeVariableDetails extends TypeDetails {
	String getIdentifier();

	ClassDetails getDeclaringType();

	List<TypeDetails> getBounds();

	boolean matches(TypeVariableDetails typeVariable);

	@Override
	default Kind getTypeKind() {
		return Kind.TYPE_VARIABLE;
	}

	@Override
	default TypeVariableDetails asTypeVariable() {
		return this;
	}
}
