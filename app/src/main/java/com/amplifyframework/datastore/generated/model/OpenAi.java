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
  public static final QueryField TOKEN_COUNT = field("OpenAi", "token_count");
  public static final QueryField ENABLE_IMAGE_VARIATIONS = field("OpenAi", "enable_image_variations");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String open_ai;
  private final @ModelField(targetType="Int") Integer token_count;
  private final @ModelField(targetType="Boolean") Boolean enable_image_variations;
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
  
  public Integer getTokenCount() {
      return token_count;
  }
  
  public Boolean getEnableImageVariations() {
      return enable_image_variations;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private OpenAi(String id, String open_ai, Integer token_count, Boolean enable_image_variations) {
    this.id = id;
    this.open_ai = open_ai;
    this.token_count = token_count;
    this.enable_image_variations = enable_image_variations;
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
              ObjectsCompat.equals(getTokenCount(), openAi.getTokenCount()) &&
              ObjectsCompat.equals(getEnableImageVariations(), openAi.getEnableImageVariations()) &&
              ObjectsCompat.equals(getCreatedAt(), openAi.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), openAi.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getOpenAi())
      .append(getTokenCount())
      .append(getEnableImageVariations())
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
      .append("token_count=" + String.valueOf(getTokenCount()) + ", ")
      .append("enable_image_variations=" + String.valueOf(getEnableImageVariations()) + ", ")
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
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      open_ai,
      token_count,
      enable_image_variations);
  }
  public interface OpenAiStep {
    BuildStep openAi(String openAi);
  }
  

  public interface BuildStep {
    OpenAi build();
    BuildStep id(String id);
    BuildStep tokenCount(Integer tokenCount);
    BuildStep enableImageVariations(Boolean enableImageVariations);
  }
  

  public static class Builder implements OpenAiStep, BuildStep {
    private String id;
    private String open_ai;
    private Integer token_count;
    private Boolean enable_image_variations;
    public Builder() {
      
    }
    
    private Builder(String id, String open_ai, Integer token_count, Boolean enable_image_variations) {
      this.id = id;
      this.open_ai = open_ai;
      this.token_count = token_count;
      this.enable_image_variations = enable_image_variations;
    }
    
    @Override
     public OpenAi build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new OpenAi(
          id,
          open_ai,
          token_count,
          enable_image_variations);
    }
    
    @Override
     public BuildStep openAi(String openAi) {
        Objects.requireNonNull(openAi);
        this.open_ai = openAi;
        return this;
    }
    
    @Override
     public BuildStep tokenCount(Integer tokenCount) {
        this.token_count = tokenCount;
        return this;
    }
    
    @Override
     public BuildStep enableImageVariations(Boolean enableImageVariations) {
        this.enable_image_variations = enableImageVariations;
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
    private CopyOfBuilder(String id, String openAi, Integer tokenCount, Boolean enableImageVariations) {
      super(id, open_ai, token_count, enable_image_variations);
      Objects.requireNonNull(open_ai);
    }
    
    @Override
     public CopyOfBuilder openAi(String openAi) {
      return (CopyOfBuilder) super.openAi(openAi);
    }
    
    @Override
     public CopyOfBuilder tokenCount(Integer tokenCount) {
      return (CopyOfBuilder) super.tokenCount(tokenCount);
    }
    
    @Override
     public CopyOfBuilder enableImageVariations(Boolean enableImageVariations) {
      return (CopyOfBuilder) super.enableImageVariations(enableImageVariations);
    }
  }
  

  public static class OpenAiIdentifier extends ModelIdentifier<OpenAi> {
    private static final long serialVersionUID = 1L;
    public OpenAiIdentifier(String id) {
      super(id);
    }
  }
  
}
