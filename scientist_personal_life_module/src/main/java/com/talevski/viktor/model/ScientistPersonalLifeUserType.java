package com.talevski.viktor.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.sql.Types.JAVA_OBJECT;
import static java.sql.Types.OTHER;
import static org.springframework.util.ObjectUtils.nullSafeEquals;

public class ScientistPersonalLifeUserType implements UserType {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int[] sqlTypes() {
        return new int[]{JAVA_OBJECT};
    }

    @Override
    public Class<ScientistPersonalLifeResult> returnedClass() {
        return ScientistPersonalLifeResult.class;
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        return nullSafeEquals(o, o1);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        final String cellContent = resultSet.getString(strings[0]);
        if (cellContent == null) return null;
        try {
            return objectMapper.readValue(cellContent.getBytes(UTF_8), returnedClass());
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert String to Invoice: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (o == null) {
            preparedStatement.setNull(i, OTHER);
            return;
        }
        try {
            final StringWriter stringWriter = new StringWriter();
            objectMapper.writeValue(stringWriter, o);
            stringWriter.flush();
            preparedStatement.setObject(i, stringWriter.toString(), OTHER);
        } catch (final Exception exception) {
            throw new RuntimeException("Failed to convert Invoice to String: " + exception.getMessage(), exception);
        }
    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();
            objectOutputStream.close();
            byteArrayOutputStream.close();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            return new ObjectInputStream(byteArrayInputStream).readObject();
        } catch (ClassNotFoundException | IOException ioException) {
            throw new HibernateException(ioException);
        }
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        return (Serializable) this.deepCopy(o);
    }

    @Override
    public Object assemble(Serializable serializable, Object o) throws HibernateException {
        return this.deepCopy(serializable);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return this.deepCopy(original);
    }
}
