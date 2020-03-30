package xiaolin.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xiaolin.entities.Type;

@Repository
public interface ITypeRepository extends JpaRepository<Type, Long> {

    @Query(value = "SELECT t FROM Type t WHERE t.typeName = :name")
    Type getTypeBaseOnTypeName(@Param("name") String typeName);
}
