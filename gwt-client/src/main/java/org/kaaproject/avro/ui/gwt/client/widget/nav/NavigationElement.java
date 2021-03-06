/*
 * Copyright 2014-2015 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaaproject.avro.ui.gwt.client.widget.nav;

import org.kaaproject.avro.ui.gwt.client.AvroUiResources.AvroUiStyle;
import org.kaaproject.avro.ui.gwt.client.util.Utils;
import org.kaaproject.avro.ui.gwt.client.widget.AbstractFieldWidget;
import org.kaaproject.avro.ui.gwt.client.widget.ArrayFieldWidget;
import org.kaaproject.avro.ui.gwt.client.widget.AvroWidgetsConfig;
import org.kaaproject.avro.ui.gwt.client.widget.RecordFieldWidget;
import org.kaaproject.avro.ui.gwt.client.widget.UnionFieldWidget;
import org.kaaproject.avro.ui.shared.ArrayField;
import org.kaaproject.avro.ui.shared.FormField;
import org.kaaproject.avro.ui.shared.RecordField;
import org.kaaproject.avro.ui.shared.UnionField;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class NavigationElement {
    
    private final NavigationContainer container;
    private final int index;
    private final NavLink link;
    private final FormField field;
    private final ScrollPanel widget = new ScrollPanel();
    private final AbstractFieldWidget<?> fieldWidget;
    private final NavigationAction action;
    private final NavigationActionListener listener;
    private Button addButton;
    private boolean added = false;
    
    public NavigationElement(AvroWidgetsConfig config, AvroUiStyle style, NavigationContainer container, int index, 
            FormField field, NavigationAction action, NavigationActionListener listener) {
        this.container = container;
        this.index = index;
        this.field = field;
        this.action = action;
        this.listener = listener;
        String title = field.getDisplayName();
        if (action == NavigationAction.ADD) {
            title = Utils.messages.addNewEntry(title);
        } else {
            added = true;
        }
        link = new NavLink(title);
        VerticalPanel verticalPanel = new VerticalPanel();
        
        boolean readOnly = action == NavigationAction.VIEW;
        
        switch (field.getFieldType()) {
        case RECORD:
            RecordFieldWidget recordFieldWidget = new RecordFieldWidget(config, style, container, readOnly);
            recordFieldWidget.setValue((RecordField)field);
            if (!readOnly) {
                recordFieldWidget.addValueChangeHandler(new ValueChangeHandler<RecordField>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<RecordField> event) {
                        valueChanged();
                    }
                });
            }
            fieldWidget = recordFieldWidget;
            verticalPanel.add(recordFieldWidget);
            break;
        case ARRAY:
            ArrayFieldWidget arrayFieldWidget = new ArrayFieldWidget(config, style, container, readOnly);
            arrayFieldWidget.setValue((ArrayField)field);
            if (!readOnly) {
                arrayFieldWidget.addValueChangeHandler(new ValueChangeHandler<ArrayField>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<ArrayField> event) {
                        valueChanged();
                    }
                });
            }
            fieldWidget = arrayFieldWidget;
            verticalPanel.add(arrayFieldWidget);
            break;
        case UNION:
            UnionFieldWidget unionFieldWidget = new UnionFieldWidget(config, style, container, readOnly);
            unionFieldWidget.setValue((UnionField)field);
            unionFieldWidget.setOpen(true);
            if (!readOnly) {
                unionFieldWidget.addValueChangeHandler(new ValueChangeHandler<UnionField>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<UnionField> event) {
                        valueChanged();
                    }
                });
            }
            fieldWidget = unionFieldWidget;
            verticalPanel.add(unionFieldWidget);
            break;
        default:
            fieldWidget = null;
            break;
        }
        
        HorizontalPanel buttonsPanel = new HorizontalPanel();
        buttonsPanel.addStyleName(style.buttonsPanel());
        if (action == NavigationAction.ADD) {
            addButton = new Button(Utils.constants.add());
            addButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    valueAdded();
                }
            });
            addButton.setEnabled(field.isValid());
            buttonsPanel.add(addButton);
        }
        if (index > 0) {
            Button backButton = new Button(Utils.constants.back());
            backButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    NavigationElement.this.container.goBack();
                }
            });
            buttonsPanel.add(backButton);
        }
        
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        verticalPanel.add(buttonsPanel);

        widget.add(verticalPanel);

        link.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                NavigationElement.this.container.gotoIndex(NavigationElement.this.index);
            }
        });
    }
    
    public NavLink getLink() {
        return link;
    }
    
    public Widget getWidget() {
        return widget;
    }
    
    public AbstractFieldWidget<?> getFieldWidget() {
        return fieldWidget;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void onShown() {
        fieldWidget.onShown();
    }
    
    public String mayClose() {
        if (!added) {
            return Utils.messages.detailsMayCloseMessage(field.getDisplayName());
        }
        return null;
    }
    
    public boolean isAdded () {
        return added;
    }
    
    private void valueAdded() {
        if (listener != null) {
            listener.onAdded(field);
        }
        added = true;
        container.goBack();
    }
    
    private void valueChanged() {
        if (action == NavigationAction.EDIT) {
            if (listener != null) {
                listener.onChanged(field);
            }
        } else {
            addButton.setEnabled(field.isValid());
        }
    }
    
}