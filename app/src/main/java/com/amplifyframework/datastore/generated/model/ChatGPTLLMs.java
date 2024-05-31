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
  public static final QueryField LLM_VERSION = field("ChatGPTLLMs", "llm_version");
  public static final QueryField SUBSCRIBED = field("ChatGPTLLMs", "subscribed");
  public static final QueryField DISPLAY_NAME = field("ChatGPTLLMs", "display_name");
  public static final QueryField SORT_ORDER = field("ChatGPTLLMs", "sort_order");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String llm_version;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean subscribed;
  private final @ModelField(targetType="String") String display_name;
  private final @ModelField(targetType="Int") Integer sort_order;
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
  
  public String getLlmVersion() {
      return llm_version;
  }
  
  public Boolean getSubscribed() {
      return subscribed;
  }
  
  public String getDisplayName() {
      return display_name;
  }
  
  public Integer getSortOrder() {
      return sort_order;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private ChatGPTLLMs(String id, String llm_version, Boolean subscribed, String display_name, Integer sort_order) {
    this.id = id;
    this.llm_version = llm_version;
    this.subscribed = subscribed;
    this.display_name = display_name;
    this.sort_order = sort_order;
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
              ObjectsCompat.equals(getLlmVersion(), chatGptllMs.getLlmVersion()) &&
              ObjectsCompat.equals(getSubscribed(), chatGptllMs.getSubscribed()) &&
              ObjectsCompat.equals(getDisplayName(), chatGptllMs.getDisplayName()) &&
              ObjectsCompat.equals(getSortOrder(), chatGptllMs.getSortOrder()) &&
              ObjectsCompat.equals(getCreatedAt(), chatGptllMs.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), chatGptllMs.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getLlmVersion())
      .append(getSubscribed())
      .append(getDisplayName())
      .append(getSortOrder())
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
      .append("llm_version=" + String.valueOf(getLlmVersion()) + ", ")
      .append("subscribed=" + String.valueOf(getSubscribed()) + ", ")
      .append("display_name=" + String.valueOf(getDisplayName()) + ", ")
      .append("sort_order=" + String.valueOf(getSortOrder()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static LlmVersionStep builder() {
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
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      llm_version,
      subscribed,
      display_name,
      sort_order);
  }
  public interface LlmVersionStep {
    SubscribedStep llmVersion(String llmVersion);
  }
  

  public interface SubscribedStep {
    BuildStep subscribed(Boolean subscribed);
  }
  

  public interface BuildStep {
    ChatGPTLLMs build();
    BuildStep id(String id);
    BuildStep displayName(String displayName);
    BuildStep sortOrder(Integer sortOrder);
  }
  

  public static class Builder implements LlmVersionStep, SubscribedStep, BuildStep {
    private String id;
    private String llm_version;
    private Boolean subscribed;
    private String display_name;
    private Integer sort_order;
    public Builder() {
      
    }
    
    private Builder(String id, String llm_version, Boolean subscribed, String display_name, Integer sort_order) {
      this.id = id;
      this.llm_version = llm_version;
      this.subscribed = subscribed;
      this.display_name = display_name;
      this.sort_order = sort_order;
    }
    
    @Override
     public ChatGPTLLMs build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new ChatGPTLLMs(
          id,
          llm_version,
          subscribed,
          display_name,
          sort_order);
    }
    
    @Override
     public SubscribedStep llmVersion(String llmVersion) {
        Objects.requireNonNull(llmVersion);
        this.llm_version = llmVersion;
        return this;
    }
    
    @Override
     public BuildStep subscribed(Boolean subscribed) {
        Objects.requireNonNull(subscribed);
        this.subscribed = subscribed;
        return this;
    }
    
    @Override
     public BuildStep displayName(String displayName) {
        this.display_name = displayName;
        return this;
    }
    
    @Override
     public BuildStep sortOrder(Integer sortOrder) {
        this.sort_order = sortOrder;
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
    private CopyOfBuilder(String id, String llmVersion, Boolean subscribed, String displayName, Integer sortOrder) {
      super(id, llm_version, subscribed, display_name, sort_order);
      Objects.requireNonNull(llm_version);
      Objects.requireNonNull(subscribed);
    }
    
    @Override
     public CopyOfBuilder llmVersion(String llmVersion) {
      return (CopyOfBuilder) super.llmVersion(llmVersion);
    }
    
    @Override
     public CopyOfBuilder subscribed(Boolean subscribed) {
      return (CopyOfBuilder) super.subscribed(subscribed);
    }
    
    @Override
     public CopyOfBuilder displayName(String displayName) {
      return (CopyOfBuilder) super.displayName(displayName);
    }
    
    @Override
     public CopyOfBuilder sortOrder(Integer sortOrder) {
      return (CopyOfBuilder) super.sortOrder(sortOrder);
    }
  }
  

  public static class ChatGPTLLMsIdentifier extends ModelIdentifier<ChatGPTLLMs> {
    private static final long serialVersionUID = 1L;
    public ChatGPTLLMsIdentifier(String id) {
      super(id);
    }
  }
  
}
