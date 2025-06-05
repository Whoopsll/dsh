package com.nh.dsh.admin.controller;

import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.model.dto.BookDTO;
import com.nh.dsh.admin.model.dto.BookResourceDTO;
import com.nh.dsh.admin.model.query.BookExchangeQuery;
import com.nh.dsh.admin.model.query.BookQuery;
import com.nh.dsh.admin.model.query.BookResourceQuery;
import com.nh.dsh.admin.model.vo.*;
import com.nh.dsh.admin.security.cache.TokenStoreCache;
import com.nh.dsh.admin.service.AuditService;
import com.nh.dsh.admin.service.BookExchangeService;
import com.nh.dsh.admin.service.BookResourceService;
import com.nh.dsh.admin.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 13:00
 **/

@Tag(name = "书本管理接口")
@AllArgsConstructor
@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;
    private final BookResourceService bookResourceService;
    private final TokenStoreCache tokenStoreCache;
    private final BookExchangeService bookExchangeService;

    @PostMapping("page")
    @PreAuthorize("hasAuthority('sys:book:view')")
    public Result<PageResult<BookVO>> page(@RequestBody BookQuery query, @RequestHeader("Authorization")String authorization) {
        return Result.ok(bookService.page(query, authorization));
    }

    @PostMapping("save")
    @PreAuthorize("hasAuthority('sys:book:add')")
    public Result<String> save(@RequestBody @Valid BookDTO dto) {
        bookService.saveBook(dto);
        return Result.ok();
    }

    @PostMapping("update")
    @PreAuthorize("hasAuthority('sys:book:edit')")
    public Result<String> update(@RequestBody @Valid BookDTO dto) {
        bookService.updateBook(dto);
        return Result.ok();
    }

    @PostMapping("/delete")
    @Operation(summary = "删除")
    @PreAuthorize("hasAuthority('sys:book:remove')")
    public Result<String> delete(@RequestBody List<Integer> ids) {
        bookService.deleteBookInBatch(ids);
        return Result.ok();
    }

    @PostMapping("list")
    @PreAuthorize("hasAuthority('sys:book:view')")
    public Result<List<BookVO>> page(@RequestHeader("Authorization")String authorization) {
        return Result.ok(bookService.selectedList(authorization));
    }

    @PostMapping("resource/save")
    @Operation(summary = "新增资源")
    @PreAuthorize("hasAuthority('sys:book:add')")
    public Result<String> saveResource(@RequestBody @Valid BookResourceDTO dto, @RequestHeader("Authorization")String authorization) {
        dto.setUserId(tokenStoreCache.getUser(authorization).getPkId());
        bookResourceService.save(dto);
        return Result.ok();
    }

    @PostMapping("resource/update")
    @Operation(summary = "修改资源")
    @PreAuthorize("hasAuthority('sys:book:add')")
    public Result<String> updateResource(@RequestBody @Valid BookResourceDTO dto) {
        bookResourceService.update(dto);
        return Result.ok();
    }

    @PostMapping("resource/enabled")
    @Operation(summary = "资源启用禁用")
    @PreAuthorize("hasAuthority('sys:book:add')")
    public Result<String> resourceEnabled(@RequestBody @Valid BookResourceDTO dto) {
        bookResourceService.resourceEnabled(dto);
        return Result.ok();
    }

    @PostMapping("resource/page")
    @Operation(summary = "资源列表")
    @PreAuthorize("hasAuthority('sys:book:view')")
    public Result<PageResult<BookResourceVO>> resourcePage(@RequestBody BookResourceQuery query) {
        PageResult<BookResourceVO> page = bookResourceService.page(query);
        return Result.ok(page);
    }

    @PostMapping("resource/delete")
    @Operation(summary = "删除资源")
    @PreAuthorize("hasAuthority('sys:book:add')")
    public Result<String> saveResource(@RequestBody List<Integer> ids) {
        bookResourceService.deleteInBatch(ids);
        return Result.ok();
    }

    @PostMapping("resource/download")
    @Operation(summary = "下载资源")
    @PreAuthorize("hasAuthority('sys:book:view')")
    public ResponseEntity<byte[]> downloadResourcesQrCode(@RequestParam(name = "bookId") Integer bookId) {
        return bookResourceService.downloadResourcesQrCode(bookId);
    }

    @PostMapping("resource/import")
    @Operation(summary = "导入资源")
    @PreAuthorize("hasAuthority('sys:book:add')")
    public Result<String> importResource(@RequestParam(name = "bookId") String bookId,@RequestParam("type") int type, @RequestParam("file") MultipartFile file) {
        bookResourceService.importResources(Integer.parseInt(bookId), file, type);
        return Result.ok();
    }

    @PostMapping("resource/auditPage")
    @Operation(summary = "资源列表")
    @PreAuthorize("hasAuthority('sys:book:view')")
    public Result<PageResult<AuditResourceVO>> auditResourcePage(@RequestBody BookResourceQuery query) {
        PageResult<AuditResourceVO> page = bookResourceService.auditResourcePage(query);
        return Result.ok(page);
    }

    @PostMapping("exchange/import")
    @Operation(summary = "书链导入")
    @PreAuthorize("hasAuthority('sys:book:view')")
    public Result<String> bookLinkImport(@RequestParam(name = "bookId") String bookId, @RequestParam("file") MultipartFile file) {
        bookExchangeService.bookLinkImport(Integer.parseInt(bookId), file);
        return Result.ok();
    }

    @PostMapping("exchange/page")
    @Operation(summary = "二维码列表")
    @PreAuthorize("hasAuthority('sys:book:view')")
    public Result<PageResult<BookExchangeVO>> exchangePage(@RequestBody BookExchangeQuery query) {
        return Result.ok(bookExchangeService.page(query));
    }

    // @GetMapping("exchange/{id}")
    // @Operation(summary = "兑换")
    public Result<String> exchange(@PathVariable(name = "id") Integer id) {
        bookExchangeService.exchange(id);
        return Result.ok();
    }

    @PostMapping("exchange/delete")
    @Operation(summary = "删除资源兑换")
    @PreAuthorize("hasAuthority('sys:book:add')")
    public Result<String> removeExchange(@RequestBody List<Integer> ids) {
        bookExchangeService.deleteInBatch(ids);
        return Result.ok();
    }

    @PostMapping("exchange/download")
    @Operation(summary = "下载兑换资源")
    @PreAuthorize("hasAuthority('sys:book:view')")
    public ResponseEntity<byte[]> downloadExchangeQrCode(@RequestParam(name = "bookId") Integer bookId) {
        return bookExchangeService.downloadExchangeQrCode(bookId);
    }

    @PostMapping("exchange/downloadXlsx")
    @Operation(summary = "下载兑换资源")
    @PreAuthorize("hasAuthority('sys:book:view')")
    public void downloadExchangeXlsx(@RequestParam(name = "bookId") Integer bookId, HttpServletResponse response) {
        bookExchangeService.downloadExchangeXlsx(bookId, response);
    }

    @PostMapping("/sticky/book/{bookId}")
    @Operation(summary = "置顶书籍")
    public Result stickyBookById(@PathVariable("bookId") int bookId) {
        return Result.ok(bookService.stickyBook(bookId));
    }
}
