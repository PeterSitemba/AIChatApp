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

/** This is an auto generated class representing the TokensRemote type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "TokensRemotes", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class TokensRemote implements Model {
  public static final QueryField ID = field("TokensRemote", "id");
  public static final QueryField REMAINING_COUNT = field("TokensRemote", "remaining_count");
  public static final QueryField USER_ID = field("TokensRemote", "user_id");
  public static final QueryField UNLIMITED = field("TokensRemote", "unlimited");
  public static final QueryField DATE = field("TokensRemote", "date");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Int", isRequired = true) Integer remaining_count;
  private final @ModelField(targetType="String", isRequired = true) String user_id;
  private final @ModelField(targetType="Boolean", isRequired = true) Boolean unlimited;
  private final @ModelField(targetType="AWSDate", isRequired = true) Temporal.Date date;
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
  
  public Integer getRemainingCount() {
      return remaining_count;
  }
  
  public String getUserId() {
      return user_id;
  }
  
  public Boolean getUnlimited() {
      return unlimited;
  }
  
  public Temporal.Date getDate() {
      return date;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private TokensRemote(String id, Integer remaining_count, String user_id, Boolean unlimited, Temporal.Date date) {
    this.id = id;
    this.remaining_count = remaining_count;
    this.user_id = user_id;
    this.unlimited = unlimited;
    this.date = date;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      TokensRemote tokensRemote = (TokensRemote) obj;
      return ObjectsCompat.equals(getId(), tokensRemote.getId()) &&
              ObjectsCompat.equals(getRemainingCount(), tokensRemote.getRemainingCount()) &&
              ObjectsCompat.equals(getUserId(), tokensRemote.getUserId()) &&
              ObjectsCompat.equals(getUnlimited(), tokensRemote.getUnlimited()) &&
              ObjectsCompat.equals(getDate(), tokensRemote.getDate()) &&
              ObjectsCompat.equals(getCreatedAt(), tokensRemote.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), tokensRemote.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getRemainingCount())
      .append(getUserId())
      .append(getUnlimited())
      .append(getDate())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("TokensRemote {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("remaining_count=" + String.valueOf(getRemainingCount()) + ", ")
      .append("user_id=" + String.valueOf(getUserId()) + ", ")
      .append("unlimited=" + String.valueOf(getUnlimited()) + ", ")
      .append("date=" + String.valueOf(getDate()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static RemainingCountStep builder() {
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
  public static TokensRemote justId(String id) {
    return new TokensRemote(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      remaining_count,
      user_id,
      unlimited,
      date);
  }
  public interface RemainingCountStep {
    UserIdStep remainingCount(Integer remainingCount);
  }
  

  public interface UserIdStep {
    UnlimitedStep userId(String userId);
  }
  

  public interface UnlimitedStep {
    DateStep unlimited(Boolean unlimited);
  }
  

  public interface DateStep {
    BuildStep date(Temporal.Date date);
  }
  

  public interface BuildStep {
    TokensRemote build();
    BuildStep id(String id);
  }
  

  public static class Builder implements RemainingCountStep, UserIdStep, UnlimitedStep, DateStep, BuildStep {
    private String id;
    private Integer remaining_count;
    private String user_id;
    private Boolean unlimited;
    private Temporal.Date date;
    public Builder() {
      
    }
    
    private Builder(String id, Integer remaining_count, String user_id, Boolean unlimited, Temporal.Date date) {
      this.id = id;
      this.remaining_count = remaining_count;
      this.user_id = user_id;
      this.unlimited = unlimited;
      this.date = date;
    }
    
    @Override
     public TokensRemote build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new TokensRemote(
          id,
          remaining_count,
          user_id,
          unlimited,
          date);
    }
    
    @Override
     public UserIdStep remainingCount(Integer remainingCount) {
        Objects.requireNonNull(remainingCount);
        this.remaining_count = remainingCount;
        return this;
    }
    
    @Override
     public UnlimitedStep userId(String userId) {
        Objects.requireNonNull(userId);
        this.user_id = userId;
        return this;
    }
    
    @Override
     public DateStep unlimited(Boolean unlimited) {
        Objects.requireNonNull(unlimited);
        this.unlimited = unlimited;
        return this;
    }
    
    @Override
     public BuildStep date(Temporal.Date date) {
        Objects.requireNonNull(date);
        this.date = date;
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
    private CopyOfBuilder(String id, Integer remainingCount, String userId, Boolean unlimited, Temporal.Date date) {
      super(id, remaining_count, user_id, unlimited, date);
      Objects.requireNonNull(remaining_count);
      Objects.requireNonNull(user_id);
      Objects.requireNonNull(unlimited);
      Objects.requireNonNull(date);
    }
    
    @Override
     public CopyOfBuilder remainingCount(Integer remainingCount) {
      return (CopyOfBuilder) super.remainingCount(remainingCount);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder unlimited(Boolean unlimited) {
      return (CopyOfBuilder) super.unlimited(unlimited);
    }
    
    @Override
     public CopyOfBuilder date(Temporal.Date date) {
      return (CopyOfBuilder) super.date(date);
    }
  }
  

  public static class TokensRemoteIdentifier extends ModelIdentifier<TokensRemote> {
    private static final long serialVersionUID = 1L;
    public TokensRemoteIdentifier(String id) {
      super(id);
    }
  }
  
}
