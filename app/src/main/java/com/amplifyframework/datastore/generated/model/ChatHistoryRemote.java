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
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ }),
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class ChatHistoryRemote implements Model {
  public static final QueryField ID = field("ChatHistoryRemote", "id");
  public static final QueryField ASSISTANT_TYPE = field("ChatHistoryRemote", "assistant_type");
  public static final QueryField CHAT_MESSAGE_LIST = field("ChatHistoryRemote", "chat_message_list");
  public static final QueryField USER_ID = field("ChatHistoryRemote", "user_id");
  public static final QueryField FAV = field("ChatHistoryRemote", "fav");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String assistant_type;
  private final @ModelField(targetType="ChatMessageObject") List<ChatMessageObject> chat_message_list;
  private final @ModelField(targetType="ID", isRequired = true) String user_id;
  private final @ModelField(targetType="Int", isRequired = true) Integer fav;
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
  
  public Integer getFav() {
      return fav;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private ChatHistoryRemote(String id, String assistant_type, List<ChatMessageObject> chat_message_list, String user_id, Integer fav) {
    this.id = id;
    this.assistant_type = assistant_type;
    this.chat_message_list = chat_message_list;
    this.user_id = user_id;
    this.fav = fav;
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
              ObjectsCompat.equals(getFav(), chatHistoryRemote.getFav()) &&
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
      .append(getFav())
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
      .append("fav=" + String.valueOf(getFav()) + ", ")
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
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      assistant_type,
      chat_message_list,
      user_id,
      fav);
  }
  public interface AssistantTypeStep {
    UserIdStep assistantType(String assistantType);
  }
  

  public interface UserIdStep {
    FavStep userId(String userId);
  }
  

  public interface FavStep {
    BuildStep fav(Integer fav);
  }
  

  public interface BuildStep {
    ChatHistoryRemote build();
    BuildStep id(String id);
    BuildStep chatMessageList(List<ChatMessageObject> chatMessageList);
  }
  

  public static class Builder implements AssistantTypeStep, UserIdStep, FavStep, BuildStep {
    private String id;
    private String assistant_type;
    private String user_id;
    private Integer fav;
    private List<ChatMessageObject> chat_message_list;
    public Builder() {
      
    }
    
    private Builder(String id, String assistant_type, List<ChatMessageObject> chat_message_list, String user_id, Integer fav) {
      this.id = id;
      this.assistant_type = assistant_type;
      this.chat_message_list = chat_message_list;
      this.user_id = user_id;
      this.fav = fav;
    }
    
    @Override
     public ChatHistoryRemote build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new ChatHistoryRemote(
          id,
          assistant_type,
          chat_message_list,
          user_id,
          fav);
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
     public BuildStep fav(Integer fav) {
        Objects.requireNonNull(fav);
        this.fav = fav;
        return this;
    }
    
    @Override
     public BuildStep chatMessageList(List<ChatMessageObject> chatMessageList) {
        this.chat_message_list = chatMessageList;
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
    private CopyOfBuilder(String id, String assistantType, List<ChatMessageObject> chatMessageList, String userId, Integer fav) {
      super(id, assistant_type, chat_message_list, user_id, fav);
      Objects.requireNonNull(assistant_type);
      Objects.requireNonNull(user_id);
      Objects.requireNonNull(fav);
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
     public CopyOfBuilder chatMessageList(List<ChatMessageObject> chatMessageList) {
      return (CopyOfBuilder) super.chatMessageList(chatMessageList);
    }
  }
  

  public static class ChatHistoryRemoteIdentifier extends ModelIdentifier<ChatHistoryRemote> {
    private static final long serialVersionUID = 1L;
    public ChatHistoryRemoteIdentifier(String id) {
      super(id);
    }
  }
  
}
