/* 
 * Copyright (C) 2019 ruslan.lopez
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

//function applyHighlights(text) {
//    var regularexp=new RegExp(document.getElementById('regexText').value,'g');
//    return text
//        .replace(/\n$/g, '\n\n')
//        .replace(regularexp, '<mark>$&</mark>');
//}
//
//function handleInput() {
//    var text = document.getElementById('txtTestCase').value;
//    var highlightedText = applyHighlights(text);
//    document.getElementById('highlights').innerHTML = highlightedText;
//}
//
//function handleScroll() {
//    var scrollTop = document.getElementById('txtTestCase').scrollTop();
//    document.getElementById('backdrop').scrollTop(scrollTop);
//}
//
//document.getElementById('txtTestCase').oninput = handleInput;
//
//document.getElementById('txtTestCase').onscroll = handleScroll;