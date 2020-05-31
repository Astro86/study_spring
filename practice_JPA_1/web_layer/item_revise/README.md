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

# 변경 감지와 병합(merge)

> entity가 직접적으로 넘어와야 merge를 사용한다.

## 준영속 엔티티
> 영속성 컨텍스트가 더 이상 관리하지 않는 엔티티

`itemService.saveItem(book)`에서 수정을 시도하는 `Book`객체는 이미 DB에 한번 저장되어 식별자가 존재한다. 기존의 식별자를 가지고 새로운 엔티티를 만들어도 비영속 엔티티가 아닌 준영속 엔티티라 볼 수 있다.  

준영속 상태는 JPA가 관리 하지 않는 entity다. 때문에 트랜잭션 commit시점에 dirty checking이 되지 않아 DB에 update가 일어나지 않는다.

## 준영속 엔티티를 수정하는 방법
- 변경 감지 기능을 사용
- 병합 사용

## 변경 감지 기능 사용 (준영속 -> 영속)

```java
@Transactional
public void updateItem(Long itemId, Book param){
    Item findItem = itemRepository.findOne(itemId);
    findItem.setPrice(param.getPrice());
    findItem.setName(param.getName());
    findItem.setStockQuantity(param.getStockQuantity());
}
```

`find`메소드를 통해 준영속 상태의 엔티티를 영속 상태로 바꿔주면 dirty checking이 가능하다. 트렌잭션 commit시 update 쿼리문을 만들어 데이터를 갱신해준다.

## 병합 사용

내부적으로는 변경 감지 기능과 비슷한 코드가 흘러가게 된다. DB에서 id를 이용해 해당 entity를 찾는다음 parameter값으로 entity를 바꿔준다. 그후 commit시 반영이 되게 한다.

### 주의점
변경 감지 기능을 사용하면 내가 원하는 속성만을 선택해서 변경하는 것이 가능하지만, 병합을 사용하면 모든 속성이 변경된다는 단점이 있다. 특히 병합시 값이 없으면 null로 업데이트가 될 수 있다는 위험성도 존재한다.

### 가장 좋은 해결 방법

> 최대한 merge를 사용하지 않는 쪽으로 코드를 짜야 한다. 항상 변경 감지를 사용하자!!!

- controller에서 어설프게 엔티티를 생성하지 않는다.
- 트랜잭션이 있는 서비스 계층에 식별자와 변경할 데이터를 명확하게 전달한다.
- 트랜잭션이 있는 서비스 계층에서 영속 상태의 엔티티를 조회하고, 엔티티의 데이터를 직접 변경한다.
- 트랜잭션 커밋 시점에 변경 감지가 실행된다.