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

/** This is an auto generated class representing the TestProUserManagement type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "TestProUserManagements", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class TestProUserManagement implements Model {
  public static final QueryField ID = field("TestProUserManagement", "id");
  public static final QueryField USER_ID = field("TestProUserManagement", "user_id");
  public static final QueryField EMAIL = field("TestProUserManagement", "email");
  public static final QueryField ENABLED = field("TestProUserManagement", "enabled");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String user_id;
  private final @ModelField(targetType="AWSEmail") String email;
  private final @ModelField(targetType="Boolean") Boolean enabled;
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
  
  public String getEmail() {
      return email;
  }
  
  public Boolean getEnabled() {
      return enabled;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private TestProUserManagement(String id, String user_id, String email, Boolean enabled) {
    this.id = id;
    this.user_id = user_id;
    this.email = email;
    this.enabled = enabled;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      TestProUserManagement testProUserManagement = (TestProUserManagement) obj;
      return ObjectsCompat.equals(getId(), testProUserManagement.getId()) &&
              ObjectsCompat.equals(getUserId(), testProUserManagement.getUserId()) &&
              ObjectsCompat.equals(getEmail(), testProUserManagement.getEmail()) &&
              ObjectsCompat.equals(getEnabled(), testProUserManagement.getEnabled()) &&
              ObjectsCompat.equals(getCreatedAt(), testProUserManagement.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), testProUserManagement.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserId())
      .append(getEmail())
      .append(getEnabled())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("TestProUserManagement {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("user_id=" + String.valueOf(getUserId()) + ", ")
      .append("email=" + String.valueOf(getEmail()) + ", ")
      .append("enabled=" + String.valueOf(getEnabled()) + ", ")
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
  public static TestProUserManagement justId(String id) {
    return new TestProUserManagement(
      id,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      user_id,
      email,
      enabled);
  }
  public interface BuildStep {
    TestProUserManagement build();
    BuildStep id(String id);
    BuildStep userId(String userId);
    BuildStep email(String email);
    BuildStep enabled(Boolean enabled);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String user_id;
    private String email;
    private Boolean enabled;
    public Builder() {
      
    }
    
    private Builder(String id, String user_id, String email, Boolean enabled) {
      this.id = id;
      this.user_id = user_id;
      this.email = email;
      this.enabled = enabled;
    }
    
    @Override
     public TestProUserManagement build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new TestProUserManagement(
          id,
          user_id,
          email,
          enabled);
    }
    
    @Override
     public BuildStep userId(String userId) {
        this.user_id = userId;
        return this;
    }
    
    @Override
     public BuildStep email(String email) {
        this.email = email;
        return this;
    }
    
    @Override
     public BuildStep enabled(Boolean enabled) {
        this.enabled = enabled;
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
    private CopyOfBuilder(String id, String userId, String email, Boolean enabled) {
      super(id, user_id, email, enabled);
      
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
    
    @Override
     public CopyOfBuilder enabled(Boolean enabled) {
      return (CopyOfBuilder) super.enabled(enabled);
    }
  }
  

  public static class TestProUserManagementIdentifier extends ModelIdentifier<TestProUserManagement> {
    private static final long serialVersionUID = 1L;
    public TestProUserManagementIdentifier(String id) {
      super(id);
    }
  }
  
}
