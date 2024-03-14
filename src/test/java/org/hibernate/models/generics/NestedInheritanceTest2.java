package org.hibernate.models.generics;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.models.SourceModelTestHelper;
import org.hibernate.models.internal.SourceModelBuildingContextImpl;
import org.hibernate.models.spi.ClassDetails;
import org.hibernate.models.spi.ClassTypeDetails;
import org.hibernate.models.spi.FieldDetails;
import org.hibernate.models.spi.TypeDetails;
import org.hibernate.models.spi.TypeVariableDetails;

import org.junit.jupiter.api.Test;

import org.jboss.jandex.Index;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKey;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Marco Belladelli
 */
public class NestedInheritanceTest2 {
	@Test
	void testNestedGenericHierarchy() {
		final SourceModelBuildingContextImpl buildingContext = SourceModelTestHelper.createBuildingContext(
				(Index) null,
				Child.class,
				Parent.class,
				ChildHierarchy1.class,
				ParentHierarchy1.class,
				ChildHierarchy2.class,
				ParentHierarchy2.class,
				ChildHierarchy22.class,
				ParentHierarchy22.class
		);

		final ClassDetails child = buildingContext.getClassDetailsRegistry().getClassDetails( Child.class.getName() );
		final ClassDetails childHierarchy22 = buildingContext.getClassDetailsRegistry().getClassDetails(
				ChildHierarchy22.class.getName() );

		final FieldDetails parent = child.findFieldByName( "parent" );
		final TypeDetails baseFieldType = parent.getType();
		assertThat( baseFieldType.getTypeKind() ).isEqualTo( TypeDetails.Kind.TYPE_VARIABLE );

		{
			final TypeDetails resolvedParentType = parent.resolveRelativeType( childHierarchy22 );
			assertThat( resolvedParentType ).isInstanceOf( ClassTypeDetails.class );
			final ClassDetails concreteClassDetails = ( (ClassTypeDetails) resolvedParentType ).getClassDetails();
			assertThat( concreteClassDetails.toJavaClass() ).isEqualTo( ParentHierarchy22.class );
		}
	}

	static abstract class Child<P extends Parent> {
		P parent;
	}

	static abstract class Parent<C extends Child> {
		Map<Long, C> children;
	}

	static class ParentHierarchy1 extends Parent<ChildHierarchy1> {
	}

	static class ChildHierarchy1 extends Child<ParentHierarchy1> {
	}

	static class ChildHierarchy2<P extends ParentHierarchy2> extends Child<P> {
	}

	static class ParentHierarchy2<C extends ChildHierarchy2> extends Parent<C> {
	}

	static class ChildHierarchy22 extends ChildHierarchy2<ParentHierarchy22> {
	}

	static class ParentHierarchy22 extends ParentHierarchy2<ChildHierarchy22> {
	}
}
