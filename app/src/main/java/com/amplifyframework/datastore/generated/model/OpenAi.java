package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the OpenAi type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "OpenAis", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class OpenAi implements Model {
  public static final QueryField ID = field("OpenAi", "id");
  public static final QueryField OPEN_AI = field("OpenAi", "open_ai");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String open_ai;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public String getOpenAi() {
      return open_ai;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private OpenAi(String id, String open_ai) {
    this.id = id;
    this.open_ai = open_ai;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      OpenAi openAi = (OpenAi) obj;
      return ObjectsCompat.equals(getId(), openAi.getId()) &&
              ObjectsCompat.equals(getOpenAi(), openAi.getOpenAi()) &&
              ObjectsCompat.equals(getCreatedAt(), openAi.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), openAi.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getOpenAi())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("OpenAi {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("open_ai=" + String.valueOf(getOpenAi()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static OpenAiStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static OpenAi justId(String id) {
    return new OpenAi(
      id,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      open_ai);
  }
  public interface OpenAiStep {
    BuildStep openAi(String openAi);
  }
  

  public interface BuildStep {
    OpenAi build();
    BuildStep id(String id);
  }
  

  public static class Builder implements OpenAiStep, BuildStep {
    private String id;
    private String open_ai;
    public Builder() {
      
    }
    
    private Builder(String id, String open_ai) {
      this.id = id;
      this.open_ai = open_ai;
    }
    
    @Override
     public OpenAi build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new OpenAi(
          id,
          open_ai);
    }
    
    @Override
     public BuildStep openAi(String openAi) {
        Objects.requireNonNull(openAi);
        this.open_ai = openAi;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String openAi) {
      super(id, open_ai);
      Objects.requireNonNull(open_ai);
    }
    
    @Override
     public CopyOfBuilder openAi(String openAi) {
      return (CopyOfBuilder) super.openAi(openAi);
    }
  }
  

  public static class OpenAiIdentifier extends ModelIdentifier<OpenAi> {
    private static final long serialVersionUID = 1L;
    public OpenAiIdentifier(String id) {
      super(id);
    }
  }
  
}
