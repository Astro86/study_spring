# 상품 수정

> 상품 수정과 관련된 컨트롤러

```java
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping(value = "/items/new")
    public String create(BookForm form){
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping(value = "/items")
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping(value = "/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping(value = "/items/{itemsId}/edit")
    public String updateItem(@ModelAttribute("form")BookForm form){
        Book book = new Book();
        book.setId(form.getId());
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);

        return "redirect:/items";
    }
}
```

Form에서 넘어 올 때 악의적인 누군가가 Id를 조작해서 넘길 수 있기 때문에 조심해야 한다. 그러면 다른 사람의 데이터가 변경 될 수 있다. 때문에 권한을 확인하는 로직이 필요하다.

```java
@Transactional
public void saveItem(Item item){
    itemRepository.save(item);
}
```

`itemService.saveItem(book);`를 실행하면 @Transaction이 걸린 상태로 넘어가게 된다.


> 상품 수정 관련 폼

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="fragments/header :: header"/>
<body>
<div class="container">
    <div th:replace="fragments/bodyHeader :: bodyHeader"/>
    <form th:object="${form}" method="post">         
        <!-- id --> 
        <input type="hidden" th:field="*{id}"/>
        <div class="form-group">
            <label th:for="name">상품명</label> 
            <input type="text" th:field="*{name}" class="form-control" placeholder="이름을 입력하세요"/>
        </div>
        <div class="form-group">
            <label th:for="price">가격</label> 
            <input type="number" th:field="*{price}" class="form-control" placeholder="가격을 입력하세요"/>
        </div>
        <div class="form-group">
            <label th:for="stockQuantity">수량</label> 
            <input type="number" th:field="*{stockQuantity}" class="form- control" placeholder="수량을 입력하세요"/>
        </div>
        <div class="form-group">
            <label th:for="author">저자</label> 
            <input type="text" th:field="*{author}" class="form-control" placeholder="저자를 입력하세요"/>
        </div>
        <div class="form-group">
            <label th:for="isbn">ISBN</label> 
            <input type="text" th:field="*{isbn}" class="form-control" placeholder="ISBN을 입력하세요"/></div>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
    <div th:replace="fragments/footer :: footer"/>
</div> <!-- /container -->
</body>
</html>
```