import { ChatInput } from "@/components/custom/chatinput";
import { PreviewMessage, ThinkingMessage } from "../../components/custom/message";
import { useScrollToBottom } from '@/components/custom/use-scroll-to-bottom';
import { useState } from "react";
import { message } from "../../interfaces/interfaces"
import { Overview } from "@/components/custom/overview";
import { Header } from "@/components/custom/header";
import {v4 as uuidv4} from 'uuid';
import axios from 'axios';
import '@/styles/main.css';

const API_URL = "http://localhost:8090/chat"; // change to your API endpoint

export function Chat() {
  const [messagesContainerRef, messagesEndRef] = useScrollToBottom<HTMLDivElement>();
  const [messages, setMessages] = useState<message[]>([]);
  const [question, setQuestion] = useState<string>("");
  const [isLoading, setIsLoading] = useState<boolean>(false);

  async function handleSubmit(text?: string) {
    if (isLoading) return;

    const messageText = text || question;
    setIsLoading(true);
    
    const traceId = uuidv4();
    setMessages(prev => [...prev, { content: messageText, role: "user", id: traceId }]);
    setQuestion("");

    try {
      const response = await axios.post(API_URL, {
        message: messageText
      });

      setMessages(prev => [
        ...prev,
        { content: response.data.message, role: "assistant", id: traceId }
      ]);
    } catch (error) {
      console.error("API error:", error);
      // Optional: Add error handling UI here
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <div className="app-container">
      <Header/>
      <div className="messages-container" ref={messagesContainerRef}>
        {messages.length == 0 && <Overview />}
        {messages.map((message, index) => (
          <PreviewMessage key={index} message={message} />
        ))}
        {isLoading && <ThinkingMessage />}
        <div ref={messagesEndRef} className="scroll-anchor"/>
      </div>
      <div className="input-container">
        <ChatInput  
          question={question}
          setQuestion={setQuestion}
          onSubmit={handleSubmit}
          isLoading={isLoading}
        />
      </div>
    </div>
  );
};