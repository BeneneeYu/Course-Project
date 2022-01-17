package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Writer;
import org.springframework.data.repository.CrudRepository;

public interface WriterRepository extends CrudRepository<Writer,Long> {
    Writer findByWriterNameAndEmail(String writerName,String email);
}
