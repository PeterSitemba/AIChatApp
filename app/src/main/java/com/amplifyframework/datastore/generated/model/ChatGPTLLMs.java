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

/** This is an auto generated class representing the ChatGPTLLMs type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "ChatGPTLLMs", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class ChatGPTLLMs implements Model {
  public static final QueryField ID = field("ChatGPTLLMs", "id");
  public static final QueryField LLM_VERSIONS = field("ChatGPTLLMs", "llm_versions");
  public static final QueryField SUBSRIBED = field("ChatGPTLLMs", "subsribed");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) List<String> llm_versions;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean subsribed;
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
  
  public List<String> getLlmVersions() {
      return llm_versions;
  }
  
  public Boolean getSubsribed() {
      return subsribed;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private ChatGPTLLMs(String id, List<String> llm_versions, Boolean subsribed) {
    this.id = id;
    this.llm_versions = llm_versions;
    this.subsribed = subsribed;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      ChatGPTLLMs chatGptllMs = (ChatGPTLLMs) obj;
      return ObjectsCompat.equals(getId(), chatGptllMs.getId()) &&
              ObjectsCompat.equals(getLlmVersions(), chatGptllMs.getLlmVersions()) &&
              ObjectsCompat.equals(getSubsribed(), chatGptllMs.getSubsribed()) &&
              ObjectsCompat.equals(getCreatedAt(), chatGptllMs.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), chatGptllMs.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getLlmVersions())
      .append(getSubsribed())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("ChatGPTLLMs {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("llm_versions=" + String.valueOf(getLlmVersions()) + ", ")
      .append("subsribed=" + String.valueOf(getSubsribed()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static LlmVersionsStep builder() {
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
  public static ChatGPTLLMs justId(String id) {
    return new ChatGPTLLMs(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      llm_versions,
      subsribed);
  }
  public interface LlmVersionsStep {
    SubsribedStep llmVersions(List<String> llmVersions);
  }
  

  public interface SubsribedStep {
    BuildStep subsribed(Boolean subsribed);
  }
  

  public interface BuildStep {
    ChatGPTLLMs build();
    BuildStep id(String id);
  }
  

  public static class Builder implements LlmVersionsStep, SubsribedStep, BuildStep {
    private String id;
    private List<String> llm_versions;
    private Boolean subsribed;
    public Builder() {
      
    }
    
    private Builder(String id, List<String> llm_versions, Boolean subsribed) {
      this.id = id;
      this.llm_versions = llm_versions;
      this.subsribed = subsribed;
    }
    
    @Override
     public ChatGPTLLMs build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new ChatGPTLLMs(
          id,
          llm_versions,
          subsribed);
    }
    
    @Override
     public SubsribedStep llmVersions(List<String> llmVersions) {
        Objects.requireNonNull(llmVersions);
        this.llm_versions = llmVersions;
        return this;
    }
    
    @Override
     public BuildStep subsribed(Boolean subsribed) {
        Objects.requireNonNull(subsribed);
        this.subsribed = subsribed;
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
    private CopyOfBuilder(String id, List<String> llmVersions, Boolean subsribed) {
      super(id, llm_versions, subsribed);
      Objects.requireNonNull(llm_versions);
      Objects.requireNonNull(subsribed);
    }
    
    @Override
     public CopyOfBuilder llmVersions(List<String> llmVersions) {
      return (CopyOfBuilder) super.llmVersions(llmVersions);
    }
    
    @Override
     public CopyOfBuilder subsribed(Boolean subsribed) {
      return (CopyOfBuilder) super.subsribed(subsribed);
    }
  }
  

  public static class ChatGPTLLMsIdentifier extends ModelIdentifier<ChatGPTLLMs> {
    private static final long serialVersionUID = 1L;
    public ChatGPTLLMsIdentifier(String id) {
      super(id);
    }
  }
  
}
