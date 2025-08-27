# Quiz Module DTOs and Mappers

This document provides an overview of the DTOs and mappers created for the Quiz module.

## Structure Created

```
src/main/java/com/example/quiz_boot/modules/quiz/
├── dto/
│   ├── request/
│   │   ├── CategoryCreateDto.java
│   │   ├── CategoryUpdateDto.java
│   │   ├── QuestionCreateDto.java
│   │   ├── QuestionUpdateDto.java
│   │   ├── QuestionOptionCreateDto.java
│   │   ├── QuestionOptionUpdateDto.java
│   │   ├── QuizCreateDto.java
│   │   └── QuizUpdateDto.java
│   └── response/
│       ├── CategoryResponseDto.java
│       ├── CategorySummaryDto.java
│       ├── QuestionResponseDto.java
│       ├── QuestionSummaryDto.java
│       ├── QuestionOptionResponseDto.java
│       ├── QuizResponseDto.java
│       ├── QuizSummaryDto.java
│       └── QuizDetailDto.java
└── mapper/
    ├── CategoryMapper.java
    ├── QuestionMapper.java
    ├── QuestionOptionMapper.java
    └── QuizMapper.java
```

## DTOs Overview

### Request DTOs

- **CategoryCreateDto/UpdateDto**: For creating and updating categories
- **QuestionCreateDto/UpdateDto**: For creating and updating questions with validation
- **QuestionOptionCreateDto/UpdateDto**: For creating and updating question options
- **QuizCreateDto/UpdateDto**: For creating and updating quizzes with nested questions

### Response DTOs

- **Summary DTOs**: Lightweight versions for listing views (CategorySummaryDto, QuestionSummaryDto, QuizSummaryDto)
- **Response DTOs**: Full data for detailed views (CategoryResponseDto, QuestionResponseDto, QuizResponseDto)
- **Detail DTOs**: Complete data with all relationships (QuizDetailDto)

## Key Features

### Validation

- All create DTOs include comprehensive validation annotations
- Size constraints match entity constraints
- Required fields properly marked with @NotNull/@NotBlank

### Type Safety

- Uses proper Java types (Instant for timestamps, matching BaseEntity)
- Boolean handling for nullable fields
- Proper collection handling for nested relationships

### Mappers

- Spring @Component annotation for dependency injection
- Simple conversion methods without over-engineering
- Update methods for partial updates
- List conversion utilities
- Null-safe operations

### Dependencies Used

- **Lombok**: @Data, @AllArgsConstructor, @NoArgsConstructor for boilerplate reduction
- **Jakarta Validation**: @NotNull, @NotBlank, @Size, @Min, @Max for validation
- **Spring Framework**: @Component, @Autowired for dependency injection

## Usage Examples

### Creating a Quiz

```java
QuizCreateDto dto = new QuizCreateDto();
dto.setTitle("Java Basics Quiz");
dto.setDescription("Test your Java knowledge");
dto.setCategoryId(1L);
dto.setCreatorId(1L);
// Set other properties...

Quiz quiz = quizMapper.toEntity(dto);
```

### Converting to Response

```java
Quiz quiz = quizRepository.findById(1L);
QuizResponseDto response = quizMapper.toResponseDto(quiz);
```

### Updating Entity

```java
Quiz existingQuiz = quizRepository.findById(1L);
QuizUpdateDto updateDto = new QuizUpdateDto();
updateDto.setTitle("Updated Title");

quizMapper.updateEntity(existingQuiz, updateDto);
```

## Design Decisions

1. **No Over-engineering**: Simple POJO DTOs with standard Spring patterns
2. **Validation at DTO Level**: Comprehensive validation to catch issues early
3. **Multiple Response Types**: Summary, Response, and Detail DTOs for different use cases
4. **Mapper Pattern**: Centralized conversion logic in dedicated mapper components
5. **Null Safety**: Proper handling of optional fields and relationships
