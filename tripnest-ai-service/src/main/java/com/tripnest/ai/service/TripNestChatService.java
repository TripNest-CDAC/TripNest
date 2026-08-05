package com.tripnest.ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class TripNestChatService {
    private static final String OUT_OF_SCOPE = "I can help only with currently available TripNest packages, trips, destinations, prices, dates, and seats.";
    private final ChatClient chatClient;
    private final CatalogueContextService catalogueContextService;

    public TripNestChatService(ChatClient chatClient, CatalogueContextService catalogueContextService) {
        this.chatClient = chatClient;
        this.catalogueContextService = catalogueContextService;
    }

    public String answer(String question) {
        if (!isTravelQuestion(question)) return OUT_OF_SCOPE;
        String catalogue = catalogueContextService.activeCatalogue();
        String answer = chatClient.prompt()
                .system("""
                        You are the TripNest travel assistant. Answer only from the supplied TripNest catalogue.
                        Never invent a package, price, date, seat count, policy, or destination.
                        Do not discuss passwords, JWT, Aadhaar, payments, private users, or internal implementation.
                        If the catalogue does not contain an answer, say: 'I could not find an available TripNest result for that question.'
                        Keep answers short, helpful, and suitable for a travel website.
                        Use plain text only; never use Markdown symbols such as **, *, #, or tables.
                        For a list of packages, sort by lowest price first and use this exact layout:
                        1. Package name
                           Route: source to destination
                           Price: ₹amount per person
                           Dates: start date to end date
                           Seats: number available
                        """)
                .user("Customer question: " + question.trim() + "\n\nTripNest catalogue:\n" + catalogue)
                .call()
                .content();
        return cleanMarkdown(answer);
    }

    private boolean isTravelQuestion(String question) {
        String text = question.toLowerCase(Locale.ROOT);
        if (text.matches(".*(tourist|user|customer|company|admin|profile|email|phone|address|aadhaar|password|jwt|token|payment|transaction|booking id|booking record).*$")) return false;
        return text.matches(".*(trip|package|travel|destination|place|price|cost|budget|seat|available|date|goa|manali|jaipur|mahabaleshwar|udaipur|kochi|pune|route|holiday).*");
    }

    private String cleanMarkdown(String answer) {
        return answer.replace("**", "")
                .replaceAll("(?m)^\\s*\\*\\s*", "   ")
                .replace("*", "");
    }
}
