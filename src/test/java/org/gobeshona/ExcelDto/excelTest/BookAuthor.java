package org.gobeshona.ExcelDto.excelTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookAuthor {

    private long bookId;
    private String bookName;
    private String bookCode;
    private long authorId;
    private String authorName;
    private long authorCode;

}
