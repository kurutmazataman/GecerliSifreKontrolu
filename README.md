### **Şifre Doğrulama Kuralları**
Şifrenin geçerli olabilmesi için şu dört şartı sağlaması gerekir:

1. **Şifrenin ilk karakteri büyük harf olmalı**.
2. **Şifrenin son karakteri `?` olmalı**.
3. **Şifre en az 8 karakter uzunluğunda olmalı**.
4. **Şifre, en az bir boşluk karakteri (`' '`) içermeli**.

Eğer bu kurallardan herhangi biri ihlal edilirse, kullanıcıya uygun bir hata mesajı gösterilir ve şifreyi tekrar girmesi istenir.

### **Kodun Yapısı ve Çalışma Mantığı**

#### **1. `Check` Interface**

```java
public interface Check {
    boolean check(String str) throws Exception;
}
```

- **`Check`** interface'i, şifre doğrulama işlemini gerçekleştiren tüm sınıflar için bir temel sağlar. Her bir doğrulama sınıfı, bu interface'i implement eder ve **`check`** metodunu kendi doğrulama mantığıyla implement eder.
- Bu metot, **`String str`** parametresi alır (şifreyi temsil eder) ve şifrenin geçerli olup olmadığını kontrol eder. Şifre geçerli değilse, uygun bir **`Exception`** fırlatır.

#### **2. Somut `Check` Sınıfları**

Şifre doğrulama işlemi, dört ana kontrol sınıfı tarafından gerçekleştirilir:

##### **a. `CheckFirstCharacterIsUpperCase`**

```java
public class CheckFirstCharacterIsUpperCase implements Check {
    private Check checkNext;

    public CheckFirstCharacterIsUpperCase(Check checkNext){
        this.checkNext = checkNext;
    }

    @Override
    public boolean check(String str) throws Exception {
        if (!str.matches("^[A-Z].*")) {
            throw new FirstCharacterNotUpperCaseException();
        }
        return checkNext.check(str);
    }
}
```

- Bu sınıf, **şifrenin ilk karakterinin büyük harf olup olmadığını** kontrol eder. Eğer ilk karakter büyük harf değilse, **`FirstCharacterNotUpperCaseException`** istisnası fırlatılır.
- Eğer kontrol geçilirse, şifre **`checkNext.check(str)`** ile bir sonraki kontrol sınıfına gönderilir.

##### **b. `CheckLastCharacterEqualsToQuestionMark`**

```java
public class CheckLastCharacterEqualsToQuestionMark implements Check {
    private Check checkNext;

    public CheckLastCharacterEqualsToQuestionMark(Check checkNext){
        this.checkNext = checkNext;
    }

    @Override
    public boolean check(String str) throws Exception {
        if (!str.endsWith("?")) {
            throw new LastCharacterMismatch();
        }
        return checkNext.check(str);
    }
}
```

- Bu sınıf, **şifrenin son karakterinin `?` olup olmadığını** kontrol eder. Eğer son karakter `?` değilse, **`LastCharacterMismatch`** istisnası fırlatılır.
- Eğer geçerliyse, şifre bir sonraki kontrole (eğer varsa) gönderilir.

##### **c. `CheckLength`**

```java
public class CheckLength implements Check {
    @Override
    public boolean check(String str) throws Exception {
        if (!(str.length() >= 8)) {
            throw new LengthException();
        }
        return true;
    }
}
```

- Bu sınıf, **şifrenin uzunluğunun en az 8 karakter olup olmadığını** kontrol eder. Eğer şifre 8 karakterden kısa ise, **`LengthException`** istisnası fırlatılır.
- Şifre yeterince uzun olduğunda, işlem başarılı olur.

##### **d. `CheckSpaceCharacter`**

```java
public class CheckSpaceCharacter implements Check {
    Check checkNext;

    public CheckSpaceCharacter(Check check){
        this.checkNext = check;
    }

    @Override
    public boolean check(String str) throws Exception {
        if (!str.contains(" ")) {
            throw new NotContainSpaceCharacter();
        }
        return checkNext.check(str);
    }
}
```

- Bu sınıf, **şifrenin bir boşluk karakteri içermesi gerektiğini** kontrol eder. Eğer şifre boşluk karakteri içermiyorsa, **`NotContainSpaceCharacter`** istisnası fırlatılır.
- Eğer boşluk karakteri bulunuyorsa, şifre bir sonraki kontrol sınıfına iletilir.

#### **3. Özel İstisnalar (Exception Classes)**

Her kontrol sınıfı, belirli bir şart sağlanmazsa, uygun bir **istimlak** fırlatır. Bu istisnalar şu şekildedir:

- **`FirstCharacterNotUpperCaseException`**: İlk karakter büyük olmalı.
- **`LastCharacterMismatch`**: Son karakter `?` olmalı.
- **`NotContainSpaceCharacter`**: Şifre boşluk karakteri içermeli.
- **`LengthException`**: Şifre en az 8 karakter olmalı.

Her istisna, **`getMessage()`** metoduyla hata mesajını döner ve kullanıcıya neyi yanlış yaptığını bildirir.

#### **4. `Main` Sınıfı**

```java
public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.print("Şifre Girin: ");
            try {
                new CheckFirstCharacterIsUpperCase(new CheckLastCharacterEqualsToQuestionMark(new CheckSpaceCharacter(new CheckLength()))).check(new Scanner(System.in).nextLine());
                return;
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
```

- **`Main` sınıfı**, şifreyi kullanıcıdan alır ve doğrulama işlemini başlatır.
- **Zincirleme Kontrol Yapısı**: Bu sınıf, doğrulama işlemini sırasıyla şu şekilde başlatır:
    1. `CheckFirstCharacterIsUpperCase`
    2. `CheckLastCharacterEqualsToQuestionMark`
    3. `CheckSpaceCharacter`
    4. `CheckLength`

- **`while (true)` Döngüsü**: Kullanıcı doğru bir şifre girene kadar, şifreyi tekrar tekrar istemek için bir döngü çalıştırılır. Herhangi bir hata meydana gelirse, **hata mesajı** gösterilir ve kullanıcıdan yeniden şifre girmesi istenir.

- **`try-catch` Blokları**: Eğer şifre geçerli değilse, uygun istisna fırlatılır ve catch bloğu bu hatayı yakalayarak hata mesajını yazdırır.

### **Kodun Amacı ve İşleyişi**

Bu kodun amacı, bir şifrenin dört farklı kurala uygun olup olmadığını **kontrol etmek** ve her kuralı ayrı bir kontrol sınıfı ile modüler bir şekilde **denetlemektir**. **Chain of Responsibility (SOR)** tasarım deseni kullanarak, her bir kontrol, bir sonrakini çağırmadan önce kendi doğrulama işlemini yapar. Eğer bir kontrol başarısız olursa, bir hata mesajı fırlatılır ve kullanıcıdan tekrar şifre girmesi istenir.

**Adım Adım İşleyiş:**
1. **Şifre Girişi**: Kullanıcı, şifreyi girer.
2. **Kontrol Sınıfları**: Şifre sırasıyla şu dört kuralla kontrol edilir:
    - İlk karakter büyük harf mi?
    - Son karakter `?` mı?
    - Şifre en az 8 karakter mi?
    - Şifre boşluk içeriyor mu?
3. **Hata Mesajları**: Eğer herhangi bir kural ihlal edilirse, uygun hata mesajı yazdırılır.
4. **Geçerli Şifre**: Eğer tüm kontroller geçilirse, program sonlanır.

### **Kullanıcıya Verilen Hata Mesajları**

- "Ilk Karakter Büyük Harfle Başlamalı"
- "Son Karakter ? Olmalı"
- "Şifreniz Boşluk Karakteri İçermeli"
- "Şifreniz En Az 8 Haneli Olmalı"

### **Kodun Avantajları**

1. **Modülerlik**: Her bir doğrulama kuralı ayrı bir sınıfla tanımlandığı için sistem modülerdir ve yeni kurallar eklemek kolaydır.
2. **Esneklik**: Yeni doğrulama kuralları eklemek için yalnızca yeni bir `Check` sınıfı oluşturulabilir.
3. **Anlaşılabilirlik**: Hatalar, kullanıcıya açık ve net mesajlarla iletilir, bu da kullanıcı deneyimini iyileştirir.

Bu kodda **iki temel tasarım deseni** kullanılmıştır:

### 1. **Chain of Responsibility (SOR) Deseni**

**Chain of Responsibility** (SOR) deseni, bir nesne zincirinin (chain) belirli bir isteği işlemesi için kullanılır. Bu desende, bir nesne (ya da sınıf) bir isteği alır ve işlemi yapıp yapmama kararını verir. Eğer işlem yapılamazsa, istek bir sonraki nesneye iletilir ve bu işlem devam eder. Zincir sonuna kadar işlem yapılmazsa, hata veya sonuç döndürülebilir.

#### Kodda **Chain of Responsibility** Deseninin Kullanımı:
- Kodda, şifrenin doğrulanmasında kullanılan **`Check`** interface'ini implement eden sınıflar, her biri kendi doğrulama işlemini yapar ve sonrasında **bir sonraki kontrolü çağırır**.
- **Kontroller Zinciri**:
    - `CheckFirstCharacterIsUpperCase`
    - `CheckLastCharacterEqualsToQuestionMark`
    - `CheckSpaceCharacter`
    - `CheckLength`

  Bu sınıflar birbirine bağlıdır ve doğrulama işlemi sırasıyla yapılır. Her bir sınıf, kendi doğrulamasını yapar, eğer başarılı olursa bir sonraki sınıfa geçer. Eğer bir kural başarısız olursa, bir istisna (exception) fırlatılır ve zincir sonlanır.

#### Örnek:
- **`CheckFirstCharacterIsUpperCase`** sınıfı, şifrenin ilk karakterinin büyük harf olup olmadığını kontrol eder. Eğer bu şart sağlanırsa, kontrol bir sonraki sınıfa (`CheckLastCharacterEqualsToQuestionMark`) geçer. Bu şekilde zincir devam eder.

```java
public boolean check(String str) throws Exception {
    if (!str.matches("^[A-Z].*")) {
        throw new FirstCharacterNotUpperCaseException();
    }
    return checkNext.check(str);
}
```

#### Avantajları:
- **Genişletilebilirlik**: Yeni kontrol kuralları eklemek çok kolaydır, sadece yeni bir sınıf ekleyip, `checkNext` ile zincire dahil edebilirsiniz.
- **Esneklik**: Şifre doğrulama kurallarının sırası değiştirilebilir veya bazı kurallar kaldırılabilir, çünkü her kontrol bağımsızdır.
- **Tek sorumluluk prensibi**: Her sınıf sadece bir doğrulama kuralını denetler, bu da kodun okunabilirliğini ve bakımını artırır.

---

### 2. **Decorator Deseni**

**Decorator** deseni, bir nesnenin davranışını değiştirmek için kullanılır. Bu desen, nesnenin üzerine yeni fonksiyonellik eklemek için bir "sarmalayıcı" (wrapper) nesne kullanır. Orijinal nesne değiştirilmeden, ek işlevler eklenebilir.

#### Kodda **Decorator** Deseninin Kullanımı:
- **`Check`** interface'ini implement eden her sınıf, başka bir **`Check`** nesnesini **sarmalar** ve kendi doğrulama işlemini gerçekleştirdikten sonra, zincirdeki bir sonraki doğrulama sınıfına iletir.

  Bu, **Decorator** desenine benzer bir yapıdır çünkü her `Check` sınıfı, diğer `Check` sınıfını "sarmalar" ve **şifreyi kontrol etme** işlemine yeni bir davranış ekler. Şifre doğrulama zincirine her yeni sınıf, ek doğrulamalar ekler.

#### Örnek:
```java
public class CheckFirstCharacterIsUpperCase implements Check {
    private Check checkNext;

    public CheckFirstCharacterIsUpperCase(Check checkNext) {
        this.checkNext = checkNext;
    }

    @Override
    public boolean check(String str) throws Exception {
        if (!str.matches("^[A-Z].*")) {
            throw new FirstCharacterNotUpperCaseException();
        }
        return checkNext.check(str);
    }
}
```

Bu kodda, **`CheckFirstCharacterIsUpperCase`** sınıfı, kendi doğrulama işlemini yaptıktan sonra, doğrulamanın bir sonraki aşamasını `checkNext` nesnesine devreder. Bu, **Decorator** deseni gibi çalışır, çünkü sınıf başka bir nesneyi **sarmalar** ve davranışını genişletir.

#### Avantajları:
- **Esneklik**: Yeni doğrulama kuralları eklemek oldukça kolaydır, çünkü her yeni kural bir decorator gibi mevcut zincire eklenir.
- **İzole Edilmiş Modüller**: Her doğrulama sınıfı, yalnızca belirli bir kuralı kontrol eder ve bu sınıflar birbirinden bağımsızdır.

---

### **Sonuç olarak, kullanılan tasarım desenleri:**

1. **Chain of Responsibility (SOR)**: Şifre doğrulama işlemi sırasıyla yapılır ve her kontrol bir sonrakine geçmeden önce kendi işlemini yapar. Kontroller zinciri oluşturulur ve işlem bir sonraki sınıfa iletilir.

2. **Decorator**: Her doğrulama sınıfı, bir başka `Check` nesnesini sarmalar ve kendi doğrulama kurallarını uygulayarak işlem sonucunu bir sonraki doğrulama sınıfına iletir. Bu, dekoratör desenine benzer bir yapıdır.

Bu iki desen birlikte kullanılarak, şifre doğrulama işlemi son derece **modüler**, **esnek** ve **geliştirilebilir** hale getirilmiştir.