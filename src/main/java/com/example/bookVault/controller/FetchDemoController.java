package com.example.bookVault.controller;

import com.example.bookVault.service.FetchDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// ─── Fetch Stratejisi Test Endpoint'leri ──────────────────────────────────────
// Her endpoint farklı bir fetch stratejisi kullanır.
// Uygulama çalışırken bu endpoint'leri çağır ve console'daki SQL'lere bak.
//
// Test için: http://localhost:8080/fetch-demo/...
@RestController
@RequestMapping("/fetch-demo")
@RequiredArgsConstructor
public class FetchDemoController {

    private final FetchDemoService fetchDemoService;

    // N+1 Problem - en kötü durum
    // Console'da: 1 SELECT books + N SELECT authors
    @GetMapping("/n-plus-one")
    public Map<String, Object> nPlusOne() {
        return fetchDemoService.scenarioNPlusOne();
    }

    // JOIN FETCH ile N+1 çözümü
    // Console'da: 1 SELECT books INNER JOIN authors
    @GetMapping("/join-fetch")
    public Map<String, Object> joinFetch() {
        return fetchDemoService.scenarioJoinFetch();
    }

    // JOIN FETCH + reviews koleksiyonu
    // Console'da: 1 SELECT DISTINCT books JOIN authors LEFT JOIN reviews
    @GetMapping("/join-fetch-reviews")
    public Map<String, Object> joinFetchWithReviews() {
        return fetchDemoService.scenarioJoinFetchWithReviews();
    }

    // @EntityGraph inline - attributePaths
    // Console'da: 1 SELECT books LEFT OUTER JOIN authors
    @GetMapping("/entity-graph-inline")
    public Map<String, Object> entityGraphInline() {
        return fetchDemoService.scenarioEntityGraphInline();
    }

    // @EntityGraph inline - tüm ilişkiler
    // Console'da: 1 SELECT DISTINCT books + 4 LEFT OUTER JOIN
    @GetMapping("/entity-graph-all")
    public Map<String, Object> entityGraphAll() {
        return fetchDemoService.scenarioEntityGraphAll();
    }

    // Named @EntityGraph - Book.withAuthorAndReviews
    // Console'da: 1 SELECT book + author + reviews WHERE id = ?
    @GetMapping("/named-graph/{id}")
    public Map<String, Object> namedEntityGraph(@PathVariable Long id) {
        return fetchDemoService.scenarioNamedEntityGraph(id);
    }

    // Named @EntityGraph - Book.withAll
    // Console'da: 1 SELECT book + author + reviews + tags WHERE id = ?
    @GetMapping("/named-graph-all/{id}")
    public Map<String, Object> namedEntityGraphAll(@PathVariable Long id) {
        return fetchDemoService.scenarioNamedEntityGraphAll(id);
    }
}
