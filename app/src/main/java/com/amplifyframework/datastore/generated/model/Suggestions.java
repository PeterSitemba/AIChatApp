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

/** This is an auto generated class representing the Suggestions type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Suggestions", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Suggestions implements Model {
  public static final QueryField ID = field("Suggestions", "id");
  public static final QueryField SUGGESTION = field("Suggestions", "suggestion");
  public static final QueryField IS_IMG_SUGGESTION = field("Suggestions", "is_img_suggestion");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String suggestion;
  private final @ModelField(targetType="Boolean") Boolean is_img_suggestion;
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
  
  public String getSuggestion() {
      return suggestion;
  }
  
  public Boolean getIsImgSuggestion() {
      return is_img_suggestion;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Suggestions(String id, String suggestion, Boolean is_img_suggestion) {
    this.id = id;
    this.suggestion = suggestion;
    this.is_img_suggestion = is_img_suggestion;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Suggestions suggestions = (Suggestions) obj;
      return ObjectsCompat.equals(getId(), suggestions.getId()) &&
              ObjectsCompat.equals(getSuggestion(), suggestions.getSuggestion()) &&
              ObjectsCompat.equals(getIsImgSuggestion(), suggestions.getIsImgSuggestion()) &&
              ObjectsCompat.equals(getCreatedAt(), suggestions.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), suggestions.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getSuggestion())
      .append(getIsImgSuggestion())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Suggestions {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("suggestion=" + String.valueOf(getSuggestion()) + ", ")
      .append("is_img_suggestion=" + String.valueOf(getIsImgSuggestion()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static BuildStep builder() {
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
  public static Suggestions justId(String id) {
    return new Suggestions(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      suggestion,
      is_img_suggestion);
  }
  public interface BuildStep {
    Suggestions build();
    BuildStep id(String id);
    BuildStep suggestion(String suggestion);
    BuildStep isImgSuggestion(Boolean isImgSuggestion);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String suggestion;
    private Boolean is_img_suggestion;
    public Builder() {
      
    }
    
    private Builder(String id, String suggestion, Boolean is_img_suggestion) {
      this.id = id;
      this.suggestion = suggestion;
      this.is_img_suggestion = is_img_suggestion;
    }
    
    @Override
     public Suggestions build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Suggestions(
          id,
          suggestion,
          is_img_suggestion);
    }
    
    @Override
     public BuildStep suggestion(String suggestion) {
        this.suggestion = suggestion;
        return this;
    }
    
    @Override
     public BuildStep isImgSuggestion(Boolean isImgSuggestion) {
        this.is_img_suggestion = isImgSuggestion;
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
    private CopyOfBuilder(String id, String suggestion, Boolean isImgSuggestion) {
      super(id, suggestion, is_img_suggestion);
      
    }
    
    @Override
     public CopyOfBuilder suggestion(String suggestion) {
      return (CopyOfBuilder) super.suggestion(suggestion);
    }
    
    @Override
     public CopyOfBuilder isImgSuggestion(Boolean isImgSuggestion) {
      return (CopyOfBuilder) super.isImgSuggestion(isImgSuggestion);
    }
  }
  

  public static class SuggestionsIdentifier extends ModelIdentifier<Suggestions> {
    private static final long serialVersionUID = 1L;
    public SuggestionsIdentifier(String id) {
      super(id);
    }
  }
  
}
