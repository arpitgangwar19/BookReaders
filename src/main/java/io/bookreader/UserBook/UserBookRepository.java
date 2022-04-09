package io.bookreader.UserBook;

import org.springframework.data.cassandra.repository.CassandraRepository;

public interface UserBookRepository extends CassandraRepository<UserBookInformation, UserBooksPrimaryKey> {

}
