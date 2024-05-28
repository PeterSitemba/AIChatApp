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

/** This is an auto generated class representing the TokenManagement type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "TokenManagements", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class TokenManagement implements Model {
  public static final QueryField ID = field("TokenManagement", "id");
  public static final QueryField USER_ID = field("TokenManagement", "user_id");
  public static final QueryField IDENTITY_ID = field("TokenManagement", "identity_id");
  public static final QueryField UNLIMITED = field("TokenManagement", "unlimited");
  public static final QueryField DUMMY_KEY = field("TokenManagement", "dummy_key");
  public static final QueryField PROMPT_TOKENS = field("TokenManagement", "prompt_tokens");
  public static final QueryField COMPLETION_TOKENS = field("TokenManagement", "completion_tokens");
  public static final QueryField TOTAL_TOKENS = field("TokenManagement", "total_tokens");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String user_id;
  private final @ModelField(targetType="String", isRequired = true) String identity_id;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean unlimited;
  private final @ModelField(targetType="String") String dummy_key;
  private final @ModelField(targetType="Int", isRequired = true) Integer prompt_tokens;
  private final @ModelField(targetType="Int", isRequired = true) Integer completion_tokens;
  private final @ModelField(targetType="Int", isRequired = true) Integer total_tokens;
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
  
  public String getUserId() {
      return user_id;
  }
  
  public String getIdentityId() {
      return identity_id;
  }
  
  public Boolean getUnlimited() {
      return unlimited;
  }
  
  public String getDummyKey() {
      return dummy_key;
  }
  
  public Integer getPromptTokens() {
      return prompt_tokens;
  }
  
  public Integer getCompletionTokens() {
      return completion_tokens;
  }
  
  public Integer getTotalTokens() {
      return total_tokens;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private TokenManagement(String id, String user_id, String identity_id, Boolean unlimited, String dummy_key, Integer prompt_tokens, Integer completion_tokens, Integer total_tokens) {
    this.id = id;
    this.user_id = user_id;
    this.identity_id = identity_id;
    this.unlimited = unlimited;
    this.dummy_key = dummy_key;
    this.prompt_tokens = prompt_tokens;
    this.completion_tokens = completion_tokens;
    this.total_tokens = total_tokens;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      TokenManagement tokenManagement = (TokenManagement) obj;
      return ObjectsCompat.equals(getId(), tokenManagement.getId()) &&
              ObjectsCompat.equals(getUserId(), tokenManagement.getUserId()) &&
              ObjectsCompat.equals(getIdentityId(), tokenManagement.getIdentityId()) &&
              ObjectsCompat.equals(getUnlimited(), tokenManagement.getUnlimited()) &&
              ObjectsCompat.equals(getDummyKey(), tokenManagement.getDummyKey()) &&
              ObjectsCompat.equals(getPromptTokens(), tokenManagement.getPromptTokens()) &&
              ObjectsCompat.equals(getCompletionTokens(), tokenManagement.getCompletionTokens()) &&
              ObjectsCompat.equals(getTotalTokens(), tokenManagement.getTotalTokens()) &&
              ObjectsCompat.equals(getCreatedAt(), tokenManagement.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), tokenManagement.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserId())
      .append(getIdentityId())
      .append(getUnlimited())
      .append(getDummyKey())
      .append(getPromptTokens())
      .append(getCompletionTokens())
      .append(getTotalTokens())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("TokenManagement {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("user_id=" + String.valueOf(getUserId()) + ", ")
      .append("identity_id=" + String.valueOf(getIdentityId()) + ", ")
      .append("unlimited=" + String.valueOf(getUnlimited()) + ", ")
      .append("dummy_key=" + String.valueOf(getDummyKey()) + ", ")
      .append("prompt_tokens=" + String.valueOf(getPromptTokens()) + ", ")
      .append("completion_tokens=" + String.valueOf(getCompletionTokens()) + ", ")
      .append("total_tokens=" + String.valueOf(getTotalTokens()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static IdentityIdStep builder() {
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
  public static TokenManagement justId(String id) {
    return new TokenManagement(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      user_id,
      identity_id,
      unlimited,
      dummy_key,
      prompt_tokens,
      completion_tokens,
      total_tokens);
  }
  public interface IdentityIdStep {
    UnlimitedStep identityId(String identityId);
  }
  

  public interface UnlimitedStep {
    PromptTokensStep unlimited(Boolean unlimited);
  }
  

  public interface PromptTokensStep {
    CompletionTokensStep promptTokens(Integer promptTokens);
  }
  

  public interface CompletionTokensStep {
    TotalTokensStep completionTokens(Integer completionTokens);
  }
  

  public interface TotalTokensStep {
    BuildStep totalTokens(Integer totalTokens);
  }
  

  public interface BuildStep {
    TokenManagement build();
    BuildStep id(String id);
    BuildStep userId(String userId);
    BuildStep dummyKey(String dummyKey);
  }
  

  public static class Builder implements IdentityIdStep, UnlimitedStep, PromptTokensStep, CompletionTokensStep, TotalTokensStep, BuildStep {
    private String id;
    private String identity_id;
    private Boolean unlimited;
    private Integer prompt_tokens;
    private Integer completion_tokens;
    private Integer total_tokens;
    private String user_id;
    private String dummy_key;
    public Builder() {
      
    }
    
    private Builder(String id, String user_id, String identity_id, Boolean unlimited, String dummy_key, Integer prompt_tokens, Integer completion_tokens, Integer total_tokens) {
      this.id = id;
      this.user_id = user_id;
      this.identity_id = identity_id;
      this.unlimited = unlimited;
      this.dummy_key = dummy_key;
      this.prompt_tokens = prompt_tokens;
      this.completion_tokens = completion_tokens;
      this.total_tokens = total_tokens;
    }
    
    @Override
     public TokenManagement build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new TokenManagement(
          id,
          user_id,
          identity_id,
          unlimited,
          dummy_key,
          prompt_tokens,
          completion_tokens,
          total_tokens);
    }
    
    @Override
     public UnlimitedStep identityId(String identityId) {
        Objects.requireNonNull(identityId);
        this.identity_id = identityId;
        return this;
    }
    
    @Override
     public PromptTokensStep unlimited(Boolean unlimited) {
        Objects.requireNonNull(unlimited);
        this.unlimited = unlimited;
        return this;
    }
    
    @Override
     public CompletionTokensStep promptTokens(Integer promptTokens) {
        Objects.requireNonNull(promptTokens);
        this.prompt_tokens = promptTokens;
        return this;
    }
    
    @Override
     public TotalTokensStep completionTokens(Integer completionTokens) {
        Objects.requireNonNull(completionTokens);
        this.completion_tokens = completionTokens;
        return this;
    }
    
    @Override
     public BuildStep totalTokens(Integer totalTokens) {
        Objects.requireNonNull(totalTokens);
        this.total_tokens = totalTokens;
        return this;
    }
    
    @Override
     public BuildStep userId(String userId) {
        this.user_id = userId;
        return this;
    }
    
    @Override
     public BuildStep dummyKey(String dummyKey) {
        this.dummy_key = dummyKey;
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
    private CopyOfBuilder(String id, String userId, String identityId, Boolean unlimited, String dummyKey, Integer promptTokens, Integer completionTokens, Integer totalTokens) {
      super(id, user_id, identity_id, unlimited, dummy_key, prompt_tokens, completion_tokens, total_tokens);
      Objects.requireNonNull(identity_id);
      Objects.requireNonNull(unlimited);
      Objects.requireNonNull(prompt_tokens);
      Objects.requireNonNull(completion_tokens);
      Objects.requireNonNull(total_tokens);
    }
    
    @Override
     public CopyOfBuilder identityId(String identityId) {
      return (CopyOfBuilder) super.identityId(identityId);
    }
    
    @Override
     public CopyOfBuilder unlimited(Boolean unlimited) {
      return (CopyOfBuilder) super.unlimited(unlimited);
    }
    
    @Override
     public CopyOfBuilder promptTokens(Integer promptTokens) {
      return (CopyOfBuilder) super.promptTokens(promptTokens);
    }
    
    @Override
     public CopyOfBuilder completionTokens(Integer completionTokens) {
      return (CopyOfBuilder) super.completionTokens(completionTokens);
    }
    
    @Override
     public CopyOfBuilder totalTokens(Integer totalTokens) {
      return (CopyOfBuilder) super.totalTokens(totalTokens);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder dummyKey(String dummyKey) {
      return (CopyOfBuilder) super.dummyKey(dummyKey);
    }
  }
  

  public static class TokenManagementIdentifier extends ModelIdentifier<TokenManagement> {
    private static final long serialVersionUID = 1L;
    public TokenManagementIdentifier(String id) {
      super(id);
    }
  }
  
}
