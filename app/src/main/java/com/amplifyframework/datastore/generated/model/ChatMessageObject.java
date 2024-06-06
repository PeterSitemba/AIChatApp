package com.amplifyframework.datastore.generated.model;


import androidx.core.util.ObjectsCompat;

import java.util.Objects;
import java.util.List;

/** This is an auto generated class representing the ChatMessageObject type in your schema. */
public final class ChatMessageObject {
  private final String role;
  private final String content;
  private final Boolean is_image_prompt;
  public String getRole() {
      return role;
  }
  
  public String getContent() {
      return content;
  }
  
  public Boolean getIsImagePrompt() {
      return is_image_prompt;
  }
  
  private ChatMessageObject(String role, String content, Boolean is_image_prompt) {
    this.role = role;
    this.content = content;
    this.is_image_prompt = is_image_prompt;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      ChatMessageObject chatMessageObject = (ChatMessageObject) obj;
      return ObjectsCompat.equals(getRole(), chatMessageObject.getRole()) &&
              ObjectsCompat.equals(getContent(), chatMessageObject.getContent()) &&
              ObjectsCompat.equals(getIsImagePrompt(), chatMessageObject.getIsImagePrompt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getRole())
      .append(getContent())
      .append(getIsImagePrompt())
      .toString()
      .hashCode();
  }
  
  public static RoleStep builder() {
      return new Builder();
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(role,
      content,
      is_image_prompt);
  }
  public interface RoleStep {
    ContentStep role(String role);
  }
  

  public interface ContentStep {
    BuildStep content(String content);
  }
  

  public interface BuildStep {
    ChatMessageObject build();
    BuildStep isImagePrompt(Boolean isImagePrompt);
  }
  

  public static class Builder implements RoleStep, ContentStep, BuildStep {
    private String role;
    private String content;
    private Boolean is_image_prompt;
    public Builder() {
      
    }
    
    private Builder(String role, String content, Boolean is_image_prompt) {
      this.role = role;
      this.content = content;
      this.is_image_prompt = is_image_prompt;
    }
    
    @Override
     public ChatMessageObject build() {
        
        return new ChatMessageObject(
          role,
          content,
          is_image_prompt);
    }
    
    @Override
     public ContentStep role(String role) {
        Objects.requireNonNull(role);
        this.role = role;
        return this;
    }
    
    @Override
     public BuildStep content(String content) {
        Objects.requireNonNull(content);
        this.content = content;
        return this;
    }
    
    @Override
     public BuildStep isImagePrompt(Boolean isImagePrompt) {
        this.is_image_prompt = isImagePrompt;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String role, String content, Boolean isImagePrompt) {
      super(role, content, is_image_prompt);
      Objects.requireNonNull(role);
      Objects.requireNonNull(content);
    }
    
    @Override
     public CopyOfBuilder role(String role) {
      return (CopyOfBuilder) super.role(role);
    }
    
    @Override
     public CopyOfBuilder content(String content) {
      return (CopyOfBuilder) super.content(content);
    }
    
    @Override
     public CopyOfBuilder isImagePrompt(Boolean isImagePrompt) {
      return (CopyOfBuilder) super.isImagePrompt(isImagePrompt);
    }
  }
  
}
