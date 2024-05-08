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

/** This is an auto generated class representing the UntitledModel type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "UntitledModels", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class UntitledModel implements Model {
  public static final QueryField ID = field("UntitledModel", "id");
  public static final QueryField APP_NAME = field("UntitledModel", "app_name");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String app_name;
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
  
  public String getAppName() {
      return app_name;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private UntitledModel(String id, String app_name) {
    this.id = id;
    this.app_name = app_name;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      UntitledModel untitledModel = (UntitledModel) obj;
      return ObjectsCompat.equals(getId(), untitledModel.getId()) &&
              ObjectsCompat.equals(getAppName(), untitledModel.getAppName()) &&
              ObjectsCompat.equals(getCreatedAt(), untitledModel.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), untitledModel.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getAppName())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("UntitledModel {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("app_name=" + String.valueOf(getAppName()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static AppNameStep builder() {
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
  public static UntitledModel justId(String id) {
    return new UntitledModel(
      id,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      app_name);
  }
  public interface AppNameStep {
    BuildStep appName(String appName);
  }
  

  public interface BuildStep {
    UntitledModel build();
    BuildStep id(String id);
  }
  

  public static class Builder implements AppNameStep, BuildStep {
    private String id;
    private String app_name;
    public Builder() {
      
    }
    
    private Builder(String id, String app_name) {
      this.id = id;
      this.app_name = app_name;
    }
    
    @Override
     public UntitledModel build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new UntitledModel(
          id,
          app_name);
    }
    
    @Override
     public BuildStep appName(String appName) {
        Objects.requireNonNull(appName);
        this.app_name = appName;
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
    private CopyOfBuilder(String id, String appName) {
      super(id, app_name);
      Objects.requireNonNull(app_name);
    }
    
    @Override
     public CopyOfBuilder appName(String appName) {
      return (CopyOfBuilder) super.appName(appName);
    }
  }
  

  public static class UntitledModelIdentifier extends ModelIdentifier<UntitledModel> {
    private static final long serialVersionUID = 1L;
    public UntitledModelIdentifier(String id) {
      super(id);
    }
  }
  
}
