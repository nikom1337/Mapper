package com.thecookiezen.entitymapper;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.thecookiezen.entitymapper.dto.query.AuthorDTO;
import com.thecookiezen.entitymapper.dto.query.CategoryDTO;
import com.thecookiezen.entitymapper.dto.query.FileDTO;
import com.thecookiezen.entitymapper.dto.query.SourceDTO;
import com.thecookiezen.entitymapper.entity.Author;
import com.thecookiezen.entitymapper.entity.Category;
import com.thecookiezen.entitymapper.entity.File;
import com.thecookiezen.entitymapper.entity.Source;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nikom
 */
public class EntityToDtoMapperFactory {

	public static CategoryDTO mapCategegoryToDTO(final Category category) {
		return new Mapper<Category, CategoryDTO>() {
			public CategoryDTO apply(Category src) {
				return new CategoryDTO(src.getId(), src.getName());
			}
		}.apply(category);
	}

	public static AuthorDTO mapAuthorToDTO(final Author author) {
		return new Mapper<Author, AuthorDTO>() {
			public AuthorDTO apply(Author src) {
				return new AuthorDTO(src.getId(), src.getName(), src.getCity());
			}
		}.apply(author);
	}

	public static SourceDTO mapSourceToDTO(final Source source) {
		return new Mapper<Source, SourceDTO>() {
			public SourceDTO apply(Source src) {
				return new SourceDTO(src.getId(), src.getName(),
					Optional.fromNullable(src.getThumbnail()).transform(new Function<File, FileDTO>() {
					public FileDTO apply(File file) {
						return mapFileToDTO(file);
					}
				}).orNull());
			}
		}.apply(source);
	}

	public static FileDTO mapFileToDTO(final File file) {
		return new Mapper<File, FileDTO>() {
			public FileDTO apply(File src) {
				return new FileDTO(src.getId(), src.getName(),
					categoriesToDTO(Optional.fromNullable(src.getCategories()).or(new ArrayList<Category>())),
					authorsToDTO(Optional.fromNullable(src.getAuthors()).or(new ArrayList<Author>())),
					Optional.fromNullable(src.getSource()).transform(new Function<Source, SourceDTO>() {
						public SourceDTO apply(Source source) {
							return mapSourceToDTO(source);
						}
					}).orNull(), src.getUrl(), src.getSize(), src.getTags());
			}
		}.apply(file);
	}

	public static List<CategoryDTO> categoriesToDTO(List<Category> categories) {
		return Lists.transform(categories, new Function<Category, CategoryDTO>() {
			public CategoryDTO apply(Category category) {
				return mapCategegoryToDTO(category);
			}
		});
	}
	
	public static List<AuthorDTO> authorsToDTO(List<Author> authors) {
		return Lists.transform(authors, new Function<Author, AuthorDTO>() {
			public AuthorDTO apply(Author author) {
				return mapAuthorToDTO(author);
			}
		});
	}
}
