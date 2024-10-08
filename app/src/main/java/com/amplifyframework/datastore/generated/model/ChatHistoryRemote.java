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

/** This is an auto generated class representing the ChatHistoryRemote type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "ChatHistoryRemotes", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class ChatHistoryRemote implements Model {
  public static final QueryField ID = field("ChatHistoryRemote", "id");
  public static final QueryField ASSISTANT_TYPE = field("ChatHistoryRemote", "assistant_type");
  public static final QueryField CHAT_MESSAGE_LIST = field("ChatHistoryRemote", "chat_message_list");
  public static final QueryField USER_ID = field("ChatHistoryRemote", "user_id");
  public static final QueryField EMAIL = field("ChatHistoryRemote", "email");
  public static final QueryField FAV = field("ChatHistoryRemote", "fav");
  public static final QueryField LOCAL_DB_ID = field("ChatHistoryRemote", "local_db_id");
  public static final QueryField MODIFIED_AT = field("ChatHistoryRemote", "modified_at");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String assistant_type;
  private final @ModelField(targetType="ChatMessageObject") List<ChatMessageObject> chat_message_list;
  private final @ModelField(targetType="String", isRequired = true) String user_id;
  private final @ModelField(targetType="AWSEmail") String email;
  private final @ModelField(targetType="Int", isRequired = true) Integer fav;
  private final @ModelField(targetType="String", isRequired = true) String local_db_id;
  private final @ModelField(targetType="AWSTimestamp", isRequired = true) Temporal.Timestamp modified_at;
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
  
  public String getAssistantType() {
      return assistant_type;
  }
  
  public List<ChatMessageObject> getChatMessageList() {
      return chat_message_list;
  }
  
  public String getUserId() {
      return user_id;
  }
  
  public String getEmail() {
      return email;
  }
  
  public Integer getFav() {
      return fav;
  }
  
  public String getLocalDbId() {
      return local_db_id;
  }
  
  public Temporal.Timestamp getModifiedAt() {
      return modified_at;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private ChatHistoryRemote(String id, String assistant_type, List<ChatMessageObject> chat_message_list, String user_id, String email, Integer fav, String local_db_id, Temporal.Timestamp modified_at) {
    this.id = id;
    this.assistant_type = assistant_type;
    this.chat_message_list = chat_message_list;
    this.user_id = user_id;
    this.email = email;
    this.fav = fav;
    this.local_db_id = local_db_id;
    this.modified_at = modified_at;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      ChatHistoryRemote chatHistoryRemote = (ChatHistoryRemote) obj;
      return ObjectsCompat.equals(getId(), chatHistoryRemote.getId()) &&
              ObjectsCompat.equals(getAssistantType(), chatHistoryRemote.getAssistantType()) &&
              ObjectsCompat.equals(getChatMessageList(), chatHistoryRemote.getChatMessageList()) &&
              ObjectsCompat.equals(getUserId(), chatHistoryRemote.getUserId()) &&
              ObjectsCompat.equals(getEmail(), chatHistoryRemote.getEmail()) &&
              ObjectsCompat.equals(getFav(), chatHistoryRemote.getFav()) &&
              ObjectsCompat.equals(getLocalDbId(), chatHistoryRemote.getLocalDbId()) &&
              ObjectsCompat.equals(getModifiedAt(), chatHistoryRemote.getModifiedAt()) &&
              ObjectsCompat.equals(getCreatedAt(), chatHistoryRemote.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), chatHistoryRemote.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getAssistantType())
      .append(getChatMessageList())
      .append(getUserId())
      .append(getEmail())
      .append(getFav())
      .append(getLocalDbId())
      .append(getModifiedAt())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("ChatHistoryRemote {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("assistant_type=" + String.valueOf(getAssistantType()) + ", ")
      .append("chat_message_list=" + String.valueOf(getChatMessageList()) + ", ")
      .append("user_id=" + String.valueOf(getUserId()) + ", ")
      .append("email=" + String.valueOf(getEmail()) + ", ")
      .append("fav=" + String.valueOf(getFav()) + ", ")
      .append("local_db_id=" + String.valueOf(getLocalDbId()) + ", ")
      .append("modified_at=" + String.valueOf(getModifiedAt()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static AssistantTypeStep builder() {
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
  public static ChatHistoryRemote justId(String id) {
    return new ChatHistoryRemote(
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
      assistant_type,
      chat_message_list,
      user_id,
      email,
      fav,
      local_db_id,
      modified_at);
  }
  public interface AssistantTypeStep {
    UserIdStep assistantType(String assistantType);
  }
  

  public interface UserIdStep {
    FavStep userId(String userId);
  }
  

  public interface FavStep {
    LocalDbIdStep fav(Integer fav);
  }
  

  public interface LocalDbIdStep {
    ModifiedAtStep localDbId(String localDbId);
  }
  

  public interface ModifiedAtStep {
    BuildStep modifiedAt(Temporal.Timestamp modifiedAt);
  }
  

  public interface BuildStep {
    ChatHistoryRemote build();
    BuildStep id(String id);
    BuildStep chatMessageList(List<ChatMessageObject> chatMessageList);
    BuildStep email(String email);
  }
  

  public static class Builder implements AssistantTypeStep, UserIdStep, FavStep, LocalDbIdStep, ModifiedAtStep, BuildStep {
    private String id;
    private String assistant_type;
    private String user_id;
    private Integer fav;
    private String local_db_id;
    private Temporal.Timestamp modified_at;
    private List<ChatMessageObject> chat_message_list;
    private String email;
    public Builder() {
      
    }
    
    private Builder(String id, String assistant_type, List<ChatMessageObject> chat_message_list, String user_id, String email, Integer fav, String local_db_id, Temporal.Timestamp modified_at) {
      this.id = id;
      this.assistant_type = assistant_type;
      this.chat_message_list = chat_message_list;
      this.user_id = user_id;
      this.email = email;
      this.fav = fav;
      this.local_db_id = local_db_id;
      this.modified_at = modified_at;
    }
    
    @Override
     public ChatHistoryRemote build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new ChatHistoryRemote(
          id,
          assistant_type,
          chat_message_list,
          user_id,
          email,
          fav,
          local_db_id,
          modified_at);
    }
    
    @Override
     public UserIdStep assistantType(String assistantType) {
        Objects.requireNonNull(assistantType);
        this.assistant_type = assistantType;
        return this;
    }
    
    @Override
     public FavStep userId(String userId) {
        Objects.requireNonNull(userId);
        this.user_id = userId;
        return this;
    }
    
    @Override
     public LocalDbIdStep fav(Integer fav) {
        Objects.requireNonNull(fav);
        this.fav = fav;
        return this;
    }
    
    @Override
     public ModifiedAtStep localDbId(String localDbId) {
        Objects.requireNonNull(localDbId);
        this.local_db_id = localDbId;
        return this;
    }
    
    @Override
     public BuildStep modifiedAt(Temporal.Timestamp modifiedAt) {
        Objects.requireNonNull(modifiedAt);
        this.modified_at = modifiedAt;
        return this;
    }
    
    @Override
     public BuildStep chatMessageList(List<ChatMessageObject> chatMessageList) {
        this.chat_message_list = chatMessageList;
        return this;
    }
    
    @Override
     public BuildStep email(String email) {
        this.email = email;
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
    private CopyOfBuilder(String id, String assistantType, List<ChatMessageObject> chatMessageList, String userId, String email, Integer fav, String localDbId, Temporal.Timestamp modifiedAt) {
      super(id, assistant_type, chat_message_list, user_id, email, fav, local_db_id, modified_at);
      Objects.requireNonNull(assistant_type);
      Objects.requireNonNull(user_id);
      Objects.requireNonNull(fav);
      Objects.requireNonNull(local_db_id);
      Objects.requireNonNull(modified_at);
    }
    
    @Override
     public CopyOfBuilder assistantType(String assistantType) {
      return (CopyOfBuilder) super.assistantType(assistantType);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder fav(Integer fav) {
      return (CopyOfBuilder) super.fav(fav);
    }
    
    @Override
     public CopyOfBuilder localDbId(String localDbId) {
      return (CopyOfBuilder) super.localDbId(localDbId);
    }
    
    @Override
     public CopyOfBuilder modifiedAt(Temporal.Timestamp modifiedAt) {
      return (CopyOfBuilder) super.modifiedAt(modifiedAt);
    }
    
    @Override
     public CopyOfBuilder chatMessageList(List<ChatMessageObject> chatMessageList) {
      return (CopyOfBuilder) super.chatMessageList(chatMessageList);
    }
    
    @Override
     public CopyOfBuilder email(String email) {
      return (CopyOfBuilder) super.email(email);
    }
  }
  

  public static class ChatHistoryRemoteIdentifier extends ModelIdentifier<ChatHistoryRemote> {
    private static final long serialVersionUID = 1L;
    public ChatHistoryRemoteIdentifier(String id) {
      super(id);
    }
  }
  
}
