/*
 * Copyright 2016 Miroslav Janíček
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.classdump.luna.parser;

import org.classdump.luna.parser.ast.SourceInfo;

import java.util.Objects;

class SourceElement<T> {

	public final SourceInfo src;
	public final T elem;

	public SourceElement(SourceInfo src, T elem) {
		this.src = Objects.requireNonNull(src);
		this.elem = Objects.requireNonNull(elem);
	}

	public static <T> SourceElement<T> of(SourceInfo src, T elem) {
		return new SourceElement<>(src, elem);
	}

	public SourceInfo sourceInfo() {
		return src;
	}

	public T element() {
		return elem;
	}

}